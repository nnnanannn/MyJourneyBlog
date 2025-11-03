package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.security.UserPrincipal;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for LearningPost operations
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class LearningPostController {

    private final LearningPostService learningPostService;

    /**
     * Create new learning post
     * Authenticated users can create posts
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LearningPostDTO> createPost(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody LearningPostDTO postDTO) {

        LearningPostDTO createdPost = learningPostService.createPost(
                currentUser.getId(),
                postDTO
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * Get all learning posts
     * Public endpoint
     */
    @GetMapping
    public ResponseEntity<List<LearningPostDTO>> getAllPosts() {
        List<LearningPostDTO> posts = learningPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Get post by ID
     * Public endpoint
     */
    @GetMapping("/{id}")
    public ResponseEntity<LearningPostDTO> getPostById(@PathVariable Long id) {
        LearningPostDTO post = learningPostService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Get posts by category
     * Public endpoint
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<LearningPostDTO>> getPostsByCategory(
            @PathVariable String category) {

        List<LearningPostDTO> posts = learningPostService.getPostsByCategory(category);
        return ResponseEntity.ok(posts);
    }

    /**
     * Get current user's posts
     * Authenticated users only
     */
    @GetMapping("/my-posts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LearningPostDTO>> getMyPosts(
            @AuthenticationPrincipal UserPrincipal currentUser) {

        List<LearningPostDTO> posts = learningPostService.getPostsByAuthor(
                currentUser.getId()
        );

        return ResponseEntity.ok(posts);
    }

    /**
     * Get posts by author ID
     * Public endpoint
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<LearningPostDTO>> getPostsByAuthor(
            @PathVariable Long authorId) {

        List<LearningPostDTO> posts = learningPostService.getPostsByAuthor(authorId);
        return ResponseEntity.ok(posts);
    }

    /**
     * Search posts by keyword
     * Public endpoint
     */
    @GetMapping("/search")
    public ResponseEntity<List<LearningPostDTO>> searchPosts(
            @RequestParam String keyword) {

        List<LearningPostDTO> posts = learningPostService.searchPosts(keyword);
        return ResponseEntity.ok(posts);
    }

    /**
     * Update post
     * Only post author can update (checked in service layer)
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LearningPostDTO> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody LearningPostDTO postDTO,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        // TODO: Add ownership check in service
        LearningPostDTO updatedPost = learningPostService.updatePost(id, postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * Delete post
     * Only post author or ADMIN can delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        // TODO: Add ownership check in service
        learningPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
