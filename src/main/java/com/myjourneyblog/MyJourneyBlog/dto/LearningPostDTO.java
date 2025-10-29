package com.myjourneyblog.MyJourneyBlog.dto;

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
public class LearningPostDTO {

    private Long id;

    @NotBlank
    @Size(min = 5, max = 200)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String content;

    private String keyTakeaways;
    private Integer timeSpentMinutes;
    private String category;
    private String resourcesUsed;

    // Author info
    private Long authorId;
    private String authorUsername;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
