package com.myjourneyblog.MyJourneyBlog.integration;

import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserResponseDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.exception.DuplicateResourceException;
import com.myjourneyblog.MyJourneyBlog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Rolls back transactions after each test
public class UserServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    @Test
    public void testUpdateUser_Success() {
        // 1. Setup: Get the test user created in IntegrationTestBase.setUp()
        UserResponseDTO currentUser = userService.getUserByUsername(testUsername);
        assertNotNull(currentUser, "Test user should exist");

        // 2. Action: Update the user
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .fullname("Updated Name")
                .bio("Updated Bio")
                .build();

        UserResponseDTO updatedUser = userService.updateUser(currentUser.getId(), updateDTO);

        // 3. Assertion: Verify changes
        assertEquals("Updated Name", updatedUser.getFullname());
        assertEquals("Updated Bio", updatedUser.getBio());
        assertEquals(testUsername, updatedUser.getUsername()); // Username shouldn't change
    }

    @Test
    public void testGetUserByUsername_Success() {
        // Test fetching the user created in Base
        UserResponseDTO user = userService.getUserByUsername(testUsername);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }
}
