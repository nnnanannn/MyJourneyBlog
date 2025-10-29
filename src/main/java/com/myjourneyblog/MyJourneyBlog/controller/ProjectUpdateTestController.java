package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.ProjectUpdate;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.ProjectUpdateRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test/project-updates")
@RequiredArgsConstructor
public class ProjectUpdateTestController {

    private final UserRepository userRepository;
    private final ProjectUpdateRepository projectUpdateRepository;

    @PostMapping("/setup")
    @Transactional
    public String setupTestData() {
        // Find or create user
        User user = userRepository.findByUsername("learner")
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username("learner")
                            .email("learner@example.com")
                            .password("password123")
                            .fullname("Active Learner")
                            .build();
                    return userRepository.save(newUser);
                });

        // Create project updates
        ProjectUpdate update1 = ProjectUpdate.builder()
                .title("Implemented User Authentication")
                .description("Added JWT-based authentication to the REST API")
                .projectName("Blog API")
                .updateType(UpdateType.FEATURE)
                .projectStatus(ProjectStatus.COMPLETED)
                .githubRepoUrl("https://github.com/user/blog-api")
                .githubCommitHash("abc123")
                .githubPrNumber(42)
                .technologiesUsed("Spring Security, JWT, PostgreSQL")
                .challengesFaced("Understanding JWT token validation")
                .lessonsLearned("Security is complex but essential")
                .nextSteps("Add refresh token functionality")
                .author(user)
                .build();

        ProjectUpdate update2 = ProjectUpdate.builder()
                .title("Fixed N+1 Query Problem")
                .description("Optimized database queries using JOIN FETCH")
                .projectName("Blog API")
                .updateType(UpdateType.BUG_FIX)
                .projectStatus(ProjectStatus.COMPLETED)
                .technologiesUsed("JPA, Hibernate")
                .challengesFaced("Understanding lazy loading behavior")
                .lessonsLearned("Always check query logs!")
                .author(user)
                .build();

        ProjectUpdate update3 = ProjectUpdate.builder()
                .title("Working on React Frontend")
                .description("Building responsive UI for blog application")
                .projectName("Blog Frontend")
                .updateType(UpdateType.FEATURE)
                .projectStatus(ProjectStatus.IN_PROGRESS)
                .technologiesUsed("React, TypeScript, Tailwind CSS")
                .nextSteps("Complete API integration")
                .author(user)
                .build();

        projectUpdateRepository.save(update1);
        projectUpdateRepository.save(update2);
        projectUpdateRepository.save(update3);

        return "Created 3 project updates for user";
    }

    @GetMapping("/by-author/{authorId}")
    public List<ProjectUpdate> getByAuthor(@PathVariable Long authorId) {
        return projectUpdateRepository.findByAuthorIdWithAuthor(authorId);
    }

    @GetMapping("/by-status/{status}")
    public List<ProjectUpdate> getByStatus(@PathVariable ProjectStatus status) {
        return projectUpdateRepository.findByProjectStatus(status);
    }

    @GetMapping("/by-type/{type}")
    public List<ProjectUpdate> getByType(@PathVariable UpdateType type) {
        return projectUpdateRepository.findByUpdateType(type);
    }

    @GetMapping("/by-project/{projectName}")
    public List<ProjectUpdate> getByProject(@PathVariable String projectName) {
        return projectUpdateRepository.findByProjectName(projectName);
    }

    @GetMapping("/search/{keyword}")
    public List<ProjectUpdate> search(@PathVariable String keyword) {
        return projectUpdateRepository.searchByTitleOrDescription(keyword);
    }

    @GetMapping("/with-github-pr")
    public List<ProjectUpdate> getWithGitHubPR() {
        return projectUpdateRepository.findUpdatesWithGitHubPR();
    }

    @GetMapping("/stats/user/{userId}")
    public Map<String, Object> getUserStats(@PathVariable Long userId) {
        long totalUpdates = projectUpdateRepository.countByAuthorId(userId);

        return Map.of(
                "totalUpdates", totalUpdates
        );
    }

    @GetMapping("/most-active-projects")
    public List<Object[]> getMostActiveProjects() {
        return projectUpdateRepository.findMostActiveProjects();
    }

    @GetMapping("/all-stats")
    public Map<String, Object> getAllStats() {
        long total = projectUpdateRepository.count();
        long completed = projectUpdateRepository.countByProjectStatus(ProjectStatus.COMPLETED);
        long inProgress = projectUpdateRepository.countByProjectStatus(ProjectStatus.IN_PROGRESS);
        long features = projectUpdateRepository.countByUpdateType(UpdateType.FEATURE);
        long bugFixes = projectUpdateRepository.countByUpdateType(UpdateType.BUG_FIX);

        return Map.of(
                "totalUpdates", total,
                "completed", completed,
                "inProgress", inProgress,
                "features", features,
                "bugFixes", bugFixes
        );
    }
}
