package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserResponseDTO;
import com.myjourneyblog.MyJourneyBlog.exception.DuplicateResourceException;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDTO registrationDTO;
    private User user;

    @BeforeEach
    void setUp() {
        registrationDTO = UserRegistrationDTO.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .fullname("Test User")
                .build();

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .fullname("Test User")
                .build();
    }

    @Test
    void registerUser_Success() {
        // Given
        when(userRepository.existsByUsername(registrationDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserResponseDTO result = userService.registerUser(registrationDTO);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void registerUser_DuplicateUsername_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(registrationDTO.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            userService.registerUser(registrationDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_DuplicateEmail_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(registrationDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            userService.registerUser(registrationDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserResponseDTO result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }
}
