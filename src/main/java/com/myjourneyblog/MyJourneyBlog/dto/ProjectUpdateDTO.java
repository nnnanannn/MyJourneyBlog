package com.myjourneyblog.MyJourneyBlog.dto;

import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
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

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    private String projectName;
    private UpdateType updateType;
    private ProjectStatus projectStatus;
    private String githubRepoUrl;
    private String githubCommitHash;
    private Integer githubPrNumber;
    private String githubPrData;
    private String technologiesUsed;
    private String challengesFaced;
    private String lessonsLearned;
    private String nextSteps;

    private Long authorId;
    private String authorUsername;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
