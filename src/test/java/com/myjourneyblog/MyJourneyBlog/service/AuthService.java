package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.AuthResponse;
import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.exception.DuplicateResourceException;
import com.myjourneyblog.MyJourneyBlog.model.Role;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.security.JwtTokenProvider;
import com.myjourneyblog.MyJourneyBlog.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        registrationDTO = UserRegistrationDTO.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .fullname("Test User")
                .build();
    }

    @Test
    void register_Success() {
        // Given
        when(userRepository.existsByUsername(registrationDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        User savedUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateToken(any())).thenReturn("jwt-token");

        // When
        AuthResponse response = authService.register(registrationDTO);

        // Then
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        verify(emailService).sendWelcomeEmail(any(), any(), any());
    }

    @Test
    void register_DuplicateUsername_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(registrationDTO.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            authService.register(registrationDTO);
        });

        // Verify we never tried to save
        verify(userRepository, never()).save(any(User.class));
    }
}