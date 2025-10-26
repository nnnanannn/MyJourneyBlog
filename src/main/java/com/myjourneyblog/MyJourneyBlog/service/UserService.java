package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.exception.DuplicateResourceException;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.exception.ValidationException;

import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserResponseDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserUpdateDTO;

import java.util.List;

/**
 *  Service interface for User business operations
 */

public interface UserService {

    /**
     * Register a new user
     * @param registrationDTO user registration data
     * @return created user response
     * @throws DuplicateResourceException if username or email already exist
     * @throws ValidationException if validation fails
     */
    UserResponseDTO registerUser(UserRegistrationDTO registrationDTO);

    /**
     * Get user by ID
     * @param id user ID
     * @return user response
     * @throws ResourceNotFoundException if user not found
     */
    UserResponseDTO getUserById(Long id);

    /**
     * Get user by username
     * @param username username
     * @return user response
     * @throws ResourceNotFoundException if user not found
     */
    UserResponseDTO getUserByUsername(String username);

    /**
     * Get all users
     * @return list of all users
     **/
    List<UserResponseDTO> getAllUsers();

    /**
     * Update user profile
     * @param id user ID
     * @param updateDTO update data
     * @return updated user response
     * @throws ResourceNotFoundException if user not found
     * @throws DuplicateResourceException if email already in use
     */
    UserResponseDTO updateUser(Long id, UserUpdateDTO updateDTO);

    /**
     * Delete user
     * @param id user ID
     * @throws ResourceNotFoundException if user not found
     */
    void deleteUser(Long id);

    /**
     * Check if username exists
     * @param username username to check
     * @return true if exists
     */
    boolean usernameExists(String username);

    /**
     * Check if email exists
     * @param email email to check
     * @return true if exists
     */
    boolean emailExists(String email);

}
