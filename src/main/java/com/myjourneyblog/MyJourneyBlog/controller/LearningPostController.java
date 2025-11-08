package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.ErrorResponse;
import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.dto.PageResponse;
import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.security.UserPrincipal;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Learning Posts", description = "Manage learning blog posts")
public class LearningPostController {

    private final LearningPostService learningPostService;

    /**
     * Create new learning post
     * Authenticated users can create posts
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create new post",
            description = "Create a new learning blog post. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post created successfully",
                    content = @Content(schema = @Schema(implementation = LearningPostDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
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
    @Operation(
            summary = "Get all posts",
            description = "Retrieve paginated list of learning posts with optional sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Posts retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))
            )
    })
    public ResponseEntity<PageResponse<LearningPostDTO>> getAllPosts(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(description = "Field to sort by")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction (ASC or DESC)")
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
    @Operation(
            summary = "Get post by ID",
            description = "Retrieve a specific learning post by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post found",
                    content = @Content(schema = @Schema(implementation = LearningPostDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<LearningPostDTO> getPostById(
            @Parameter(description = "Post ID")
            @PathVariable Long id) {
        LearningPostDTO post = learningPostService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Get posts by category with pagination
     * Public endpoint
     */
    @GetMapping("/category/{category}")
    @Operation(
            summary = "Get post by category",
            description = "Retrieve a specific learning post by its category"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post found",
                    content = @Content(schema = @Schema(implementation = LearningPostDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<PageResponse<LearningPostDTO>> getPostsByCategory(
            @Parameter(description = "Category name")
            @PathVariable String category,

            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page")
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
    @Operation(
            summary = "Get current user's posts",
            description = "Get current authenticated user's posts. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "My-Posts found",
                    content = @Content(schema = @Schema(implementation = LearningPostDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<PageResponse<LearningPostDTO>> getMyPosts(
            @AuthenticationPrincipal UserPrincipal currentUser,

            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page number (0-indexed)")
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
    @Operation(
            summary = "Get post by author ID",
            description = "Retrieve a specific learning post by its author ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post found",
                    content = @Content(schema = @Schema(implementation = LearningPostDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<LearningPostDTO>> getPostsByAuthor(
            @Parameter(description = "author ID")
            @PathVariable Long authorId) {

        List<LearningPostDTO> posts = learningPostService.getPostsByAuthor(authorId);
        return ResponseEntity.ok(posts);
    }

    /**
     * Search posts by keyword with pagination
     * Public endpoint
     */
    @GetMapping("/search")
    @Operation(
            summary = "Search posts",
            description = "Search posts by keyword in title or content"
    )
    public ResponseEntity<PageResponse<LearningPostDTO>> searchPosts(
            @Parameter(description = "Search keyword")
            @RequestParam String keyword,

            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page number (0-indexed)")
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
    @Operation(
            summary = "Update post by Post ID",
            description = "Update post by Post ID, authentication is required"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post updated successfully",
                    content = @Content(schema = @Schema(implementation = LearningPostDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<LearningPostDTO> updatePost(
            @Parameter(description = "Post ID")
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
    @Operation(
            summary = "Delete post by Post ID",
            description = "Delete post by Post ID, authentication is required"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post deleted successfully",
                    content = @Content(schema = @Schema(implementation = LearningPostDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "Post ID")
            @PathVariable Long id,

            @AuthenticationPrincipal UserPrincipal currentUser) {

        learningPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
