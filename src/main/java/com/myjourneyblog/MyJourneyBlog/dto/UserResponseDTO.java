package com.myjourneyblog.MyJourneyBlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *  DTO for user data in responses (no password)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User response data transfer object")
public class UserResponseDTO {

    @Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Unique username", example = "johndoe")
    private String username;

    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    @Schema(description = "Full name", example = "John Doe")
    private String fullname;

    @Schema(description = "User bio", example = "Software developer learning Spring Boot")
    private String bio;

    @Schema(description = "User profile image file",  example = "IMG file")
    private String profileImageUrl;

    @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
