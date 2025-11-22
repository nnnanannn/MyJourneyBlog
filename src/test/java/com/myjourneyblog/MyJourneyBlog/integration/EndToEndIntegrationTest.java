package com.myjourneyblog.MyJourneyBlog.integration;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end integration test
 * Simulates complete user workflow
 */
public class EndToEndIntegrationTest extends IntegrationTestBase {

    @Autowired
    private LearningPostService learningPostService;

    @Test
    public void testCompleteUserWorkflow() throws Exception {
        // User is already registered in setUp()
        User user = userRepository.findByUsername(testUsername).get();
        assertNotNull(user);

        // 1. Create learning post
        LearningPostDTO postRequest = LearningPostDTO.builder()
                .title("My Learning Journey")
                .content("Today I learned about Spring Boot")
                .category("Spring Boot")
                .updatedAt(LocalDateTime.now())
                .build();

        LearningPostDTO createdPost = learningPostService.createPost(user.getId(), postRequest);
        assertNotNull(createdPost.getId());
        assertEquals("My Learning Journey", createdPost.getTitle());

        // 2. Retrieve post
        LearningPostDTO retrievedPost = learningPostService.getPostById(createdPost.getId());
        assertEquals(createdPost.getId(), retrievedPost.getId());

        // 3. Update post
        postRequest.setTitle("My Updated Journey");
        LearningPostDTO updatedPost = learningPostService.updatePost(
                createdPost.getId(),
                postRequest
        );
        assertEquals("My Updated Journey", updatedPost.getTitle());

        // 4. Search for post
        Page<LearningPostDTO> searchResults = learningPostService.searchPosts(
                "Spring", 0, 10
        );
        assertTrue(searchResults.getTotalElements() > 0);

        // 5. Get posts by topic
        Page<LearningPostDTO> topicPosts = learningPostService.getPostsByCategory(
                "Spring Boot", PageRequest.of(0, 10)
        );
        assertTrue(topicPosts.getTotalElements() > 0);

        // 6. Delete post
        learningPostService.deletePost(createdPost.getId());

        // Verify deletion
        assertThrows(Exception.class, () -> {
            learningPostService.getPostById(createdPost.getId());
        });
    }
}