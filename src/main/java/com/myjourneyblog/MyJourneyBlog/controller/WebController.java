package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.model.ProjectUpdate;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import com.myjourneyblog.MyJourneyBlog.service.ProjectUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WebController {

    @Autowired
    private LearningPostService learningPostService;

    @Autowired
    private ProjectUpdateService projectUpdateService;

    // ==================== PUBLIC PAGES ====================

    /**
     * Home page - shows recent learning posts and project updates
     * URL: http://localhost:8080/
     * Template: index.html
     */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Get recent learning posts (latest 5, sorted by creation date)
        List<LearningPostDTO> recentPosts = learningPostService.getAllPosts()
                .stream()
                .filter(post -> post.getCreatedAt() != null) // FIX: Filter out null dates to prevent crash
                .sorted(Comparator.comparing(LearningPostDTO::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Get recent project updates (latest 5, sorted by creation date)
        List<ProjectUpdateDTO> recentUpdates = projectUpdateService.getAllUpdates()
                .stream()
                .filter(update -> update.getCreatedAt() != null) // FIX: Filter out null dates to prevent crash
                .sorted(Comparator.comparing(ProjectUpdateDTO::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Add data to model for Thymeleaf to use
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("recentUpdates", recentUpdates);
        model.addAttribute("activePage", "home");

        return "index"; // Returns templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    @GetMapping("/create-post")
    public String createPost() {
        return "create-post";
    }

    @GetMapping("/learning/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", learningPostService.getPostById(id));
        return "post-detail";
    }

    @GetMapping("/learning/by-date")
    public String learningByDate(Model model) {

        // 1. Get all posts
        List<LearningPostDTO> allPosts = learningPostService.getAllPosts();

        // 2. Group posts by Date (using CreatedAt as LocalDate)
        // Use TreeMap with reverse order to show newest dates first
        Map<LocalDate, List<LearningPostDTO>> postsByDate = allPosts.stream()
                .filter(post -> post.getCreatedAt() != null) // FIX: Filter here as well for safety
                .collect(Collectors.groupingBy(
                        post -> post.getCreatedAt().toLocalDate(),
                        () -> new TreeMap<LocalDate, List<LearningPostDTO>>(Comparator.reverseOrder()),
                        Collectors.toList()
                ));

        // 3. Add to model with the name expected by the Thymeleaf template
        model.addAttribute("postsByDate", postsByDate);
        model.addAttribute("activePage", "by-date");

        return "learning-by-date";
    }

    /**
     * Project Updates by Date Page
     */
    @GetMapping("/projects/by-date")
    public String projectsByDate(Model model) {

        // 1. Get all project updates
        List<ProjectUpdateDTO> allProjectUpdates = projectUpdateService.getAllUpdates();

        // 2. Group project updates by Date
        Map<LocalDate, List<ProjectUpdateDTO>> updatesByDate = allProjectUpdates.stream()
                .filter(update -> update.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        update -> update.getCreatedAt().toLocalDate(),
                        () -> new TreeMap<LocalDate, List<ProjectUpdateDTO>>(Comparator.reverseOrder()),
                        Collectors.toList()
                ));

        // 3. Add to model with the name expected by projects-by-date.html
        model.addAttribute("updatesByDate", updatesByDate);
        model.addAttribute("activePage", "projects");

        return "projects-by-date"; // Returns templates/projects-by-date.html
    }

    @GetMapping("/test")
    String index(Principal principal) {
        return principal != null ? "homeSignedIn" : "homeNotSignedIn";
    }
}
