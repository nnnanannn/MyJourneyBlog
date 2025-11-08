package com.myjourneyblog.MyJourneyBlog.dto;

import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateDTO {

    @Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Schema(description = "Post title", example = "Understanding Spring Security", required = true)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    @Schema(description = "Update description", example = "Feature update")
    private String description;

    @Schema(description = "Project name", example = "My Blog")
    private String projectName;

    @Schema(description = "Update Type", example = "Feature update")
    private UpdateType updateType;

    @Schema(description = "Project status", example = "ACTIVE")
    private ProjectStatus projectStatus;

    @Schema(description = "Project status", example = "ACTIVE")
    private String githubRepoUrl;

    @Schema(description = "Technologies used", example = "Spring, PostgreSQL")
    private String technologiesUsed;

    @Schema(description = "Challenges Faced", example = "Debugging..")
    private String challengesFaced;

    @Schema(description = "Lessons Learned", example = "JWT, Authentication")
    private String lessonsLearned;

    @Schema(description = "What to do next", example = "Add featuring...")
    private String nextSteps;

    @Schema(description = "Author ID", example = "1")
    private Long authorId;

    @Schema(description = "Author name", example = "John Dee")
    private String authorUsername;

    @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
