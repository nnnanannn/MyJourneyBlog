package com.myjourneyblog.MyJourneyBlog.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myjourneyblog.MyJourneyBlog.dto.LoginRequest;
import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import com.myjourneyblog.MyJourneyBlog.repository.ProjectUpdateRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base class for integration tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected LearningPostRepository learningPostRepository;

    @Autowired
    protected ProjectUpdateRepository projectUpdateRepository;

    protected String testToken;
    protected String testUsername = "testuser";
    protected String testPassword = "Test123!";

    @BeforeEach
    public void setUp() {
        // Clean database
        learningPostRepository.deleteAll();
        projectUpdateRepository.deleteAll();
        userRepository.deleteAll();

        // Register test user
        UserRegistrationDTO registerRequest = UserRegistrationDTO.builder()
                .username(testUsername)
                .email("test@example.com")
                .password(testPassword)
                .fullname("Test User")
                .build();
        authService.register(registerRequest);

        // Login and get token
        LoginRequest loginRequest = new LoginRequest(testUsername, testPassword);
        testToken = authService.login(loginRequest).getToken();
    }
}
