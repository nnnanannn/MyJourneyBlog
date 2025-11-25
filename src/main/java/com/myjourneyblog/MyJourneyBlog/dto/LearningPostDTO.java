package com.myjourneyblog.MyJourneyBlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Learning blog post data transfer object")
public class LearningPostDTO {

    @Schema(description = "Post ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Schema(description = "Post title", example = "Understanding Spring Security", required = true)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, message = "Content must be at least 10 characters")
    @Schema(description = "Post content", example = "Today I learned how Spring Security works...", required = true)
    private String content;

    @Schema(description = "Key takeaways from learning", example = "Security is important!")
    private String keyTakeaways;

    @Schema(description = "Learning category", example = "SPRING")
    private String category;

    @Schema(description = "Resources used", example = "Spring docs, Baeldung tutorials")
    private String resourcesUsed;

    // Author info
    @Schema(description = "Author ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long authorId;

    @Schema(description = "Author username", example = "johndoe", accessMode = Schema.AccessMode.READ_ONLY)
    private String authorUsername;

    @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Schema(description = "Date of learning")
    private LocalDate learningDate;
}
