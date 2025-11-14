package com.myjourneyblog.MyJourneyBlog.service.impl;

import com.myjourneyblog.MyJourneyBlog.dto.LoginRequest;
import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.AuthResponse;
import com.myjourneyblog.MyJourneyBlog.exception.DuplicateResourceException;
import com.myjourneyblog.MyJourneyBlog.exception.ValidationException;
import com.myjourneyblog.MyJourneyBlog.model.Role;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.exception.GlobalExceptionHandler;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.security.JwtTokenProvider;
import com.myjourneyblog.MyJourneyBlog.security.UserPrincipal;
import com.myjourneyblog.MyJourneyBlog.service.AuthService;
import com.myjourneyblog.MyJourneyBlog.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    @Transactional
    public AuthResponse register(UserRegistrationDTO request) {
        log.info("Registering new user with username: {}", request.getUsername());

        // Validate username availability
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed: Username already exists - {}", request.getUsername());
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        // Validate email availability
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists - {}", request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Validate password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.warn("Registration failed: Passwords do not match");
            throw new ValidationException("Passwords do not match");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullname(request.getFullname())
                .role(Role.valueOf("ROLE_USER"))
                .accountNonLocked(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        // Generate JWT token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserPrincipal.create(savedUser),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(savedUser.getRole().name())));
        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullname(savedUser.getFullname())
                .role(savedUser.getRole())
                .message("User registered successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user details
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUsername()));

            // Check if account is locked
            if (!user.isAccountNonLocked()) {
                log.warn("Login failed: Account is locked - {}", request.getUsername());
                throw new BadCredentialsException("Account is locked. Please contact support.");
            }

            // Check if account is enabled
            if (!user.isEnabled()) {
                log.warn("Login failed: Account is disabled - {}", request.getUsername());
                throw new BadCredentialsException("Account is disabled. Please verify your email.");
            }

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);

            log.info("User logged in successfully: {}", user.getUsername());

            return AuthResponse.builder()
                    .token(token)
                    .token("Bearer")
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .fullname(user.getFullname())
                    .role(user.getRole())
                    .message("Login successful")
                    .build();

        } catch (BadCredentialsException e) {
            log.warn("Login failed: Invalid credentials for user - {}", request.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getUsernameFromToken(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }

    @Override
    public AuthResponse refreshToken(String token) {
        log.info("Refreshing token");

        // Validate old token
        if (!jwtTokenProvider.validateToken(token)) {
            throw new BadCredentialsException("Invalid token");
        }

        // Extract username from old token
        String username = jwtTokenProvider.getUsernameFromToken(token);

        // Get user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // Generate new token
        String newToken = jwtTokenProvider.generateTokenFromUsername(username, user.getId(), user.getEmail());

        log.info("Token refreshed for user: {}", username);

        return AuthResponse.builder()
                .token(newToken)
                .token("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .username(user.getFullname())
                .role(user.getRole())
                .message("Token refreshed successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Changing password for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // Verify old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Password change failed: Old password is incorrect for user - {}", username);
            throw new BadCredentialsException("Old password is incorrect");
        }

        // Validate new password is different
        if (oldPassword.equals(newPassword)) {
            throw new BadCredentialsException("New password must be different from old password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", username);
    }

    @Override
    public String initiatePasswordReset(String email) {
        log.info("Initiating password reset for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();

        // Store reset token (you should add these fields to User entity)
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        userRepository.save(user);

        log.info("Password reset token generated for user: {}", user.getUsername());

        // TODO: Send reset email with token
        // emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        return resetToken;
    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {
        log.info("Resetting password with token");

//        User user = userRepository.findByResetToken(resetToken)
//                .orElseThrow(() -> {
//                    log.warn("Password reset failed: Invalid reset token");
//                    return new ResourceNotFoundException("Invalid reset token");
//                });

        Optional<User> userOptional = userRepository.findByResetToken(resetToken);

        if (userOptional.isEmpty()) {
            log.warn("Password reset failed: Invalid reset token");
            throw new ResourceNotFoundException("Invalid or expired reset token");
        }

        User user = userOptional.get();

        // Check token expiry
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Password reset failed: Token expired for user - {}", user.getUsername());
            throw new BadCredentialsException("Reset token has expired");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", user.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Override
    public void logout(String token) {
        log.info("Logging out user");

        // If you implement token blacklist, add token to blacklist here
        // tokenBlacklistService.addToBlacklist(token);

        // Clear security context
        SecurityContextHolder.clearContext();

        log.info("User logged out successfully");
    }
}