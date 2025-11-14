package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.LoginRequest;
import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.AuthResponse;
import com.myjourneyblog.MyJourneyBlog.model.User;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Register a new user
     *
     * @param request Registration details
     * @return Authentication response with JWT token
     */
    AuthResponse register(UserRegistrationDTO request);

    /**
     * Authenticate user and generate JWT token
     *
     * @param request Login credentials
     * @return Authentication response with JWT token
     */
    AuthResponse login(LoginRequest request);

    /**
     * Validate JWT token
     *
     * @param token JWT token to validate
     * @return true if token is valid
     */
    boolean validateToken(String token);

    /**
     * Get username from JWT token
     *
     * @param token JWT token
     * @return Username extracted from token
     */
    String getUsernameFromToken(String token);

    /**
     * Refresh JWT token
     *
     * @param token Old token
     * @return New authentication response with refreshed token
     */
    AuthResponse refreshToken(String token);

    /**
     * Check if username already exists
     *
     * @param username Username to check
     * @return true if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email already exists
     *
     * @param email Email to check
     * @return true if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Change user password
     *
     * @param username Username
     * @param oldPassword Current password
     * @param newPassword New password
     */
    void changePassword(String username, String oldPassword, String newPassword);

    /**
     * Reset password (for forgot password feature)
     *
     * @param email User email
     * @return Reset token
     */
    String initiatePasswordReset(String email);

    /**
     * Complete password reset
     *
     * @param resetToken Reset token
     * @param newPassword New password
     */
    void resetPassword(String resetToken, String newPassword);

    /**
     * Get current authenticated user
     *
     * @param username Username
     * @return User entity
     */
    User getCurrentUser(String username);

    /**
     * Logout user (invalidate token - if using token blacklist)
     *
     * @param token JWT token to invalidate
     */
    void logout(String token);
}