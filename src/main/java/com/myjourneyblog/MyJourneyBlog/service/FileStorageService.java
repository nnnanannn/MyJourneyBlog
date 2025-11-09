package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.config.FileStorageProperties;
import com.myjourneyblog.MyJourneyBlog.exception.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

/**
 * Service for handling file storage operations
 */
@Service
@Slf4j
public class FileStorageService {

    private final Path profileImagesLocation;
    private final Path postImagesLocation;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;

        // Initialize storage directories
        this.profileImagesLocation = Paths.get(fileStorageProperties.getProfileImagesDir())
                .toAbsolutePath().normalize();
        this.postImagesLocation = Paths.get(fileStorageProperties.getPostImagesDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.profileImagesLocation);
            Files.createDirectories(this.postImagesLocation);
            log.info("File storage directories created successfully");
        } catch (Exception ex) {
            throw new FileStorageException("Could not create upload directories", ex);
        }
    }

    /**
     * Store profile image
     */
    public String storeProfileImage(MultipartFile file, String username) {
        validateFile(file, fileStorageProperties.getMaxProfileSize());
        return storeFile(file, profileImagesLocation, "profile-" + username);
    }

    /**
     * Store post image
     */
    public String storePostImage(MultipartFile file, Long postId) {
        validateFile(file, fileStorageProperties.getMaxPostSize());
        return storeFile(file, postImagesLocation, "post-" + postId);
    }

    /**
     * Store file in specified location
     */
    private String storeFile(MultipartFile file, Path location, String prefix) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check for invalid characters
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence: " + originalFileName);
            }

            // Generate unique filename
            String fileExtension = getFileExtension(originalFileName);
            String newFileName = prefix + "-" +
                    System.currentTimeMillis() + "-" +
                    UUID.randomUUID().toString() +
                    "." + fileExtension;

            // Copy file to target location
            Path targetLocation = location.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("File stored successfully: {}", newFileName);
            return newFileName;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName, ex);
        }
    }

    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file, long maxSize) {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot upload empty file");
        }

        // Check file size
        if (file.getSize() > maxSize) {
            throw new FileStorageException(
                    String.format("File size exceeds maximum allowed size: %d bytes", maxSize)
            );
        }

        // Check file extension
        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName);

        String[] allowedExtensions = fileStorageProperties.getAllowedExtensions().split(",");
        boolean isAllowed = Arrays.stream(allowedExtensions)
                .anyMatch(ext -> ext.equalsIgnoreCase(extension));

        if (!isAllowed) {
            throw new FileStorageException(
                    String.format("File type not allowed. Allowed types: %s",
                            fileStorageProperties.getAllowedExtensions())
            );
        }

        // Validate MIME type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileStorageException("Only image files are allowed");
        }
    }

    /**
     * Get file extension from filename
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new FileStorageException("Invalid file name: " + fileName);
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Delete file
     */
    public void deleteFile(String fileName, boolean isProfileImage) {
        try {
            Path location = isProfileImage ? profileImagesLocation : postImagesLocation;
            Path filePath = location.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: {}", fileName);
        } catch (IOException ex) {
            log.error("Could not delete file: {}", fileName, ex);
        }
    }

    /**
     * Get file URL for serving
     */
    public String getFileUrl(String fileName, boolean isProfileImage) {
        String directory = isProfileImage ? "profiles" : "posts";
        return "/uploads/" + directory + "/" + fileName;
    }
}
