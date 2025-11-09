package com.myjourneyblog.MyJourneyBlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myjourneyblog.MyJourneyBlog.dto.LoginRequest;
import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AuthController
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_Success() throws Exception {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .username("integrationtest")
                .email("integration@example.com")
                .password("password123")
                .fullname("Integration Test")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("integrationtest"))
                .andExpect(jsonPath("$.email").value("integration@example.com"));
    }

    @Test
    void register_DuplicateUsername_Returns409() throws Exception {
        // Register first user
        UserRegistrationDTO dto1 = UserRegistrationDTO.builder()
                .username("duplicate")
                .email("first@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        // Try to register with same username
        UserRegistrationDTO dto2 = UserRegistrationDTO.builder()
                .username("duplicate")
                .email("second@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void register_InvalidData_Returns400() throws Exception {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .username("ab") // Too short
                .email("notanemail") // Invalid email
                .password("123") // Too short
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors").exists());
    }

    @Test
    void login_Success() throws Exception {
        // First register
        UserRegistrationDTO regDto = UserRegistrationDTO.builder()
                .username("logintest")
                .email("login@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regDto)))
                .andExpect(status().isCreated());

        // Then login
        LoginRequest loginRequest = new LoginRequest("logintest", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("logintest"));
    }

    @Test
    void login_InvalidCredentials_Returns401() throws Exception {
        LoginRequest loginRequest = new LoginRequest("nonexistent", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }
}
