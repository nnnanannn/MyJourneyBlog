package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.dto.PageResponse;
import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.security.UserPrincipal;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
     * Get all learning posts with pagination and sorting
     *
     * @param page Page number (0-indexed, default: 0)
     * @param size Page size (default: 20)
     * @param sortBy Sort field (default: createdAt)
     * @param direction Sort direction (ASC/DESC, default: DESC)
     */
    @GetMapping
    public ResponseEntity<PageResponse<LearningPostDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<LearningPostDTO> postPage = learningPostService.getAllPosts(pageable);

        return ResponseEntity.ok(PageResponse.of(postPage));
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
     * Get posts by category with pagination
     * Public endpoint
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<PageResponse<LearningPostDTO>> getPostsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<LearningPostDTO> postPage = learningPostService.getPostsByCategory(category, pageable);

        return ResponseEntity.ok(PageResponse.of(postPage));
    }

    /**
     * Get current user's posts with pagination
     * Authenticated users only
     */
    @GetMapping("/my-posts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<LearningPostDTO>> getMyPosts(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<LearningPostDTO> postPage = learningPostService.getPostsByAuthor(
                currentUser.getId(), pageable);

        return ResponseEntity.ok(PageResponse.of(postPage));
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
     * Search posts by keyword with pagination
     * Public endpoint
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<LearningPostDTO>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<LearningPostDTO> postPage = learningPostService.searchPosts(keyword, pageable);

        return ResponseEntity.ok(PageResponse.of(postPage));
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

        learningPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
