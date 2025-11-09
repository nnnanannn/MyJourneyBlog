package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.FileUploadResponse;
import com.myjourneyblog.MyJourneyBlog.security.UserPrincipal;
import com.myjourneyblog.MyJourneyBlog.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST API for file upload operations
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Upload", description = "File upload and management endpoints")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    /**
     * Upload profile image
     */
    @PostMapping("/upload/profile-image")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Upload profile image",
            description = "Upload profile image for current user. Max size: 5MB. Allowed: jpg, png, gif",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<FileUploadResponse> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        log.info("Uploading profile image for user: {}", currentUser.getUsername());

        String fileName = fileStorageService.storeProfileImage(file, currentUser.getUsername());
        String fileUrl = fileStorageService.getFileUrl(fileName, true);

        FileUploadResponse response = FileUploadResponse.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Upload post image
     */
    @PostMapping("/upload/post-image")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Upload post image",
            description = "Upload image for blog post. Max size: 10MB. Allowed: jpg, png, gif",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<FileUploadResponse> uploadPostImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Long postId) {

        log.info("Uploading post image, postId: {}", postId);

        String fileName = fileStorageService.storePostImage(file, postId);
        String fileUrl = fileStorageService.getFileUrl(fileName, false);

        FileUploadResponse response = FileUploadResponse.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Delete uploaded file
     */
    @DeleteMapping("/{fileName}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Delete uploaded file",
            description = "Delete previously uploaded file",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteFile(
            @PathVariable String fileName,
            @RequestParam(defaultValue = "false") boolean isProfileImage) {

        log.info("Deleting file: {}", fileName);
        fileStorageService.deleteFile(fileName, isProfileImage);

        return ResponseEntity.noContent().build();
    }
}
