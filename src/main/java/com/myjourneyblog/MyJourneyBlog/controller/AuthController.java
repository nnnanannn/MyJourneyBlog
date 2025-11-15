package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.*;
import com.myjourneyblog.MyJourneyBlog.security.JwtTokenProvider;
import com.myjourneyblog.MyJourneyBlog.service.AuthService;
import com.myjourneyblog.MyJourneyBlog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints (public)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthService authService;

    /**
     * Register new user
     */
    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            description = "Create a new user account and receive JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username or email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        log.info("Registration request for username: {}", registrationDTO.getUsername());

        // Register user
        //UserResponseDTO user = userService.registerUser(registrationDTO);

        // Generate JWT token
//        String token = tokenProvider.generateTokenFromUsername(
//                user.getUsername(),
//                user.getId(),
//                user.getEmail()
//        );

        // Create response with token
//        AuthResponse response = AuthResponse.builder()
//                .token(token)
//                .tokenType("Bearer")
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .message("Registration successful")
//                .build();

        log.info("Registration DTO: username={}, email={}, hasPassword={}, hasConfirmPassword={}, fullname={}",
                registrationDTO.getUsername(),
                registrationDTO.getEmail(),
                registrationDTO.getPassword() != null && !registrationDTO.getPassword().isEmpty(),
                registrationDTO.getConfirmPassword() != null && !registrationDTO.getConfirmPassword().isEmpty(),
                registrationDTO.getFullname());

        AuthResponse response = authService.register(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login (authenticate user)
     */
    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticate user and receive JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for username: {}", loginRequest.getUsername());

//        try {
//            // Authenticate user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUsername(),
//                            loginRequest.getPassword()
//                    )
//            );
//
//            // Generate JWT token
//            String token = tokenProvider.generateToken(authentication);
//
//            // Get user details
//            UserResponseDTO user = userService.getUserByUsername(loginRequest.getUsername());
//
//            // Create response with token
//            AuthResponse response = AuthResponse.builder()
//                    .token(token)
//                    .tokenType("Bearer")
//                    .username(user.getUsername())
//                    .email(user.getEmail())
//                    .message("Login successful")
//                    .build();
//
//            log.info("Login successful for user: {}", loginRequest.getUsername());
//
//            return ResponseEntity.ok(response);
//
//        } catch (AuthenticationException e) {
//            log.warn("Login failed for username: {}", loginRequest.getUsername());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(AuthResponse.builder()
//                            .message("Invalid username or password")
//                            .build());
//        }
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}