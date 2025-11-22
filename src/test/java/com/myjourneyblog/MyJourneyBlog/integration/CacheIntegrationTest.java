package com.myjourneyblog.MyJourneyBlog.integration;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for caching functionality
 */
public class CacheIntegrationTest extends IntegrationTestBase {

    @MockitoSpyBean
    private LearningPostRepository learningPostRepository;

    @Autowired
    private LearningPostService learningPostService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testPostCache_HitsCache() {
        User user = userRepository.findByUsername(testUsername).get();

        // Create post
        LearningPostDTO postRequest = LearningPostDTO.builder()
                .title("Cached Post")
                .content("Content that is definitely long enough")
                .category("Java")
                .createdAt(LocalDateTime.now())
                .build();

        var createdPost = learningPostService.createPost(user.getId() ,postRequest);
        Long postId = createdPost.getId();

        // First call - cache miss (Calls repo)
        learningPostService.getPostById(postId);

        // Second call - should hit cache (Does NOT call repo)
        learningPostService.getPostById(postId);

        // Verify repository was called only once (during the cache miss)
        verify(learningPostRepository, times(1)).findById(postId);
    }
    @Test
    public void testCacheEviction_OnUpdate() {
        User user = userRepository.findByUsername(testUsername).get();

        // Create and cache post
        LearningPostDTO postRequest = LearningPostDTO.builder()
                .title("Original Title")
                .content("Content that is definitely long enough")
                .category("Java")
                .createdAt(LocalDateTime.now())
                .build();

        var post = learningPostService.createPost(user.getId(), postRequest);
        learningPostService.getPostById(post.getId()); // Cache it

        // Update post
        postRequest.setTitle("Updated Title");
        learningPostService.updatePost(post.getId(), postRequest);

        // Get post again - should fetch from database (cache evicted)
        var updatedPost = learningPostService.getPostById(post.getId());

        assertEquals("Updated Title", updatedPost.getTitle());
    }
}
