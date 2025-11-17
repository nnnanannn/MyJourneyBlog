package com.myjourneyblog.MyJourneyBlog.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for file upload functionality
 */
public class FileUploadIntegrationTest extends IntegrationTestBase {

    @Test
    public void testProfileImageUpload_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload/profile-image")
                        .file(file)
                        .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fileName").exists())
                .andExpect(jsonPath("$.fileUrl").exists())
                .andExpect(jsonPath("$.fileType").value("image/jpeg"));
    }

    @Test
    public void testProfileImageUpload_Unauthorized() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload/profile-image")
                        .file(file))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testProfileImageUpload_InvalidFileType() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "document.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "test pdf content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload/profile-image")
                        .file(file)
                        .header("Authorization", "Bearer " + testToken))
                .andExpect(status().isBadRequest());
    }
}
