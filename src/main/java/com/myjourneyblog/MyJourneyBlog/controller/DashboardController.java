package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import com.myjourneyblog.MyJourneyBlog.repository.ProjectUpdateRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final LearningPostRepository learningPostRepository;
    private final ProjectUpdateRepository projectUpdateRepository;

    @GetMapping("/stats")
    public Map<String, Object> getOverallStats() {
        Map<String, Object> stats = new HashMap<>();

        // User stats
        stats.put("totalUsers", userRepository.count());
        stats.put("usersToday", userRepository.countUsersRegisteredToday());

        // Learning post stats
        stats.put("totalPosts", learningPostRepository.count());
        stats.put("postsThisWeek", learningPostRepository.findByCreatedAtAfter(
                LocalDateTime.now().minusDays(7)).size());

        // Project update stats
        stats.put("totalUpdates", projectUpdateRepository.count());
        stats.put("activeProjects", projectUpdateRepository.findMostActiveProjects().size());

        return stats;
    }

    @GetMapping("/user/{userId}/complete-stats")
    public Map<String, Object> getUserCompleteStats(@PathVariable Long userId) {
        Map<String, Object> stats = new HashMap<>();

        // User info
        stats.put("userId", userId);

        // Learning stats
        long postCount = learningPostRepository.countByAuthorId(userId);
        stats.put("totalPosts", postCount);

        // Project stats
        long updateCount = projectUpdateRepository.countByAuthorId(userId);

        stats.put("totalUpdates", updateCount);

        // Combined stats
        stats.put("totalActivity", postCount + updateCount);

        return stats;
    }
}