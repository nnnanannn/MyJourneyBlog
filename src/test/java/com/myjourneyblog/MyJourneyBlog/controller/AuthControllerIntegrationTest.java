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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                .confirmPassword("password123") // REQUIRED field
                .fullname("Integration Test")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print()) // Prints response on failure
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("integrationtest"));
    }

    @Test
    void register_DuplicateUsername_Returns409() throws Exception {
        // 1. Register first user (Success)
        UserRegistrationDTO dto1 = UserRegistrationDTO.builder()
                .username("duplicate")
                .email("first@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andDo(print())
                .andExpect(status().isCreated());

        // 2. Register second user with same username (Failure - 409)
        UserRegistrationDTO dto2 = UserRegistrationDTO.builder()
                .username("duplicate")
                .email("second@example.com")
                .password("password123")
                .confirmPassword("password123") // REQUIRED field
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andDo(print())
                .andExpect(status().isConflict()) // Expect 409
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void register_InvalidData_Returns400() throws Exception {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .username("ab") // Invalid: Too short
                .email("notanemail") // Invalid: Bad format
                .password("123") // Invalid: Too short
                .confirmPassword("123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // Expect 400
                .andExpect(jsonPath("$.validationErrors").exists());
    }

    @Test
    void login_Success() throws Exception {
        // 1. Register user
        UserRegistrationDTO regDto = UserRegistrationDTO.builder()
                .username("logintest")
                .email("login@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        // 2. Login
        LoginRequest loginRequest = new LoginRequest("logintest", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_InvalidCredentials_Returns401() throws Exception {
        LoginRequest loginRequest = new LoginRequest("nonexistent", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
