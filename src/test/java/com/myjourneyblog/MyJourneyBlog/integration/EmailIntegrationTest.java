package com.myjourneyblog.MyJourneyBlog.integration;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.service.EmailService;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Integration tests for email functionality
 */
public class EmailIntegrationTest extends IntegrationTestBase {

    @SpyBean
    private EmailService emailService;

    @Autowired
    private LearningPostService learningPostService;

    @Test
    public void testWelcomeEmail_SentOnRegistration() throws Exception {
        // Email should be sent during setUp() registration
        verify(emailService, times(1)).sendWelcomeEmail(
                eq("test@example.com"),
                eq(testUsername),
                eq("Test User")
        );
    }

    @Test
    public void testPostPublishedEmail_SentOnPostCreation() {
        User user = userRepository.findByUsername(testUsername).get();

        LearningPostDTO postRequest = LearningPostDTO.builder()
                .title("Test Post")
                .content("Test content")
                .title("Java")
                .createdAt(LocalDateTime.now())
                .build();

        learningPostService.createPost(user.getId(), postRequest);

        // Verify email was queued (async)
        verify(emailService, times(1)).sendPostPublishedEmail(
                eq("test@example.com"),
                eq(testUsername),
                eq("Test Post"),
                anyLong()
        );
    }
}
