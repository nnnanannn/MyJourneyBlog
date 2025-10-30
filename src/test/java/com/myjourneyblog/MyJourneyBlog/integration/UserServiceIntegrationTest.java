package com.myjourneyblog.MyJourneyBlog.integration;

import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserResponseDTO;
import com.myjourneyblog.MyJourneyBlog.exception.DuplicateResourceException;
import com.myjourneyblog.MyJourneyBlog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void testRegisterUser_Success() {
        // Given
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .fullname("Test User")
                .build();

        // When
        UserResponseDTO response = userService.registerUser(dto);

        // Then
        assertNotNull(response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    void testRegisterUser_DuplicateUsername() {
        // Given
        UserRegistrationDTO dto1 = UserRegistrationDTO.builder()
                .username("duplicate")
                .email("first@example.com")
                .password("password123")
                .build();

        UserRegistrationDTO dto2 = UserRegistrationDTO.builder()
                .username("duplicate")
                .email("second@example.com")
                .password("password123")
                .build();

        // When
        userService.registerUser(dto1);

        // Then
        assertThrows(DuplicateResourceException.class, () -> {
            userService.registerUser(dto2);
        });
    }
}
