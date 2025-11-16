package com.myjourneyblog.MyJourneyBlog.service.impl;

import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserResponseDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.exception.DuplicateResourceException;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.exception.ValidationException;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;

import com.myjourneyblog.MyJourneyBlog.service.FileStorageService;
import com.myjourneyblog.MyJourneyBlog.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  Implementation of UserService
 */

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

//    public User findByID(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +id));
//    }
//
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("Username not found: "+ username));
//    }
//
//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
//    }
//
//    @Transactional
//    public User createUser(User user) {
//        boolean usernameExists = userRepository.existsByUsername(user.getUsername());
//        boolean emailExists = userRepository.existsByEmail(user.getEmail());
//
//        // Both username and email exist
//        if(usernameExists && emailExists) {
//            throw new ValidationException("Username: " + user.getUsername() + ", and Email: " + user.getEmail() +" already exist");
//        }
//        // Only username exists
//        if (usernameExists){
//            throw new ValidationException("Username already exists: " + user.getUsername());
//        }
//        // Only email exists
//        if (emailExists) {
//            throw new ValidationException("Email already exists: " + user.getEmail());
//        }
//        // Both are unique, save the user
//        return userRepository.save(user);
//    }
//
//    @Transactional
//    public User updateUser(Long id, User userDetails) {
//        User user = findByID(id);
//
//        if (userDetails.getFullname() != null) {
//            user.setFullname(userDetails.getFullname());
//        }
//        if (userDetails.getBio() != null) {
//            user.setBio(userDetails.getBio());
//        }
//
//        // updatedAt will be set automatically by @Prepersist on User Entity
//        return userRepository.save(user);
//    }
//
//    @Transactional
//    public void deleteUserById(Long id) {
//        User user = findByID(id);
//        if (user == null) {
//            System.out.println("User not found");
//        }
//        userRepository.delete(user);
//    }
//
//    @Transactional
//    public void deleteUserByUsername(String username) {
//        User user = findByUsername(username);
//        if (user == null) {
//            System.out.println("User not found");
//        }
//        userRepository.delete(user);
//    }
//
//    @Transactional
//    public User createUserThatFails(User user) {
//        userRepository.save(user);
//        throw new RuntimeException("Simulated error");
//    }


//    @Override
//    @Transactional
//    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
//        log.info("Registering new user with username: {}", registrationDTO.getUsername());
//
//        // Validate username doesn't exist
//        // Fail fast! Don't attempt database operation if validation fails
//        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
//            log.warn("Username already exists: {}", registrationDTO.getUsername());
//            throw new DuplicateResourceException("User", "username", registrationDTO.getUsername());
//        }
//
//        // Validate email doesn't exist
//        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
//            log.warn("Email already exists: {}", registrationDTO.getEmail());
//            throw new DuplicateResourceException("User", "email", registrationDTO.getEmail());
//        }
//
//        // Additional business validation
//        validatePassword(registrationDTO.getPassword());
//
//        // Create user entity with ENCRYPTED password
//        User user = User.builder()
//                .username(registrationDTO.getUsername())
//                .email(registrationDTO.getEmail())
//                .password(passwordEncoder.encode(registrationDTO.getPassword())) // ENCRYPT!
//                .fullname(registrationDTO.getFullname())
//                .bio(registrationDTO.getBio())
//                .build();
//
//        // Save user
//        User savedUser = userRepository.save(user);
//        log.info("User registered successfully with ID: {}", savedUser.getId());
//
//        return toResponseDTO(savedUser);
//    }

    /**
     * Get user by ID with caching
     */
    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO getUserById(Long id) {
        log.debug("Fetching user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        return toResponseDTO(user);
    }

    /**
     * Get user by username with caching
     */
    @Override
    @Cacheable(value = "users", key = "'username-' + #username")
    public UserResponseDTO getUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User not found with username: %s", username)));

        return toResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.debug("Fetching all users");

        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update user profile and refresh cache
     */
    @Override
    @Transactional
    @CachePut(value = "users", key = "#userId")
    public UserResponseDTO updateUser(Long id, UserUpdateDTO updateDTO) {
        log.info("Updating user profile with ID: {}, and refreshing cache", id);

        // Find existing user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        // Update email if provided and different
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail())) {
            // Check if new email already exists
            if (userRepository.existsByEmail(updateDTO.getEmail())) {
                log.warn("Email already in use: {}", updateDTO.getEmail());
                throw new DuplicateResourceException("User", "email", updateDTO.getEmail());
            }
            user.setEmail(updateDTO.getEmail());
        }

        // Update password if provided (and encrypt it!)
        if (updateDTO.getPassword() != null) {
            validatePassword(updateDTO.getPassword());
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword())); // ENCRYPT!
        }

        // Update other fields
        if (updateDTO.getFullname() != null) {
            user.setFullname(updateDTO.getFullname());
        }

        if (updateDTO.getBio() != null) {
            user.setBio(updateDTO.getBio());
        }

        if (updateDTO.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updateDTO.getProfileImageUrl());
        }

        // Save changes (will trigger @PreUpdate)
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getId());

        return toResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        // Verify user exists
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     *
     * Validate password meets business rules
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }
        // Additional password rules to be added here later
        // Ex.
        // - Must contain uppercase letter
        // - Must contain number
        // - Must contain special character
    }

    /**
     * Update profile image and refresh cache
     */
    @Override
    @Transactional
    @CachePut(value = "users", key = "#userId")
    public UserResponseDTO updateProfileImage(Long id, String imageUrl) {
        log.info("Updating profile image: {}, and refreshing cache", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Delete old image if exists
        if (user.getProfileImageUrl() != null) {
            String oldFileName = user.getProfileImageUrl().substring(
                    user.getProfileImageUrl().lastIndexOf("/") + 1
            );
            fileStorageService.deleteFile(oldFileName, true);
        }

        user.setProfileImageUrl(imageUrl);
        User savedUser = userRepository.save(user);

        return toResponseDTO(savedUser);
    }

    /**
     * Convert User entity to UserResponseDTO
     * No Password response back to user!
     */
    private UserResponseDTO toResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .bio(user.getBio())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
