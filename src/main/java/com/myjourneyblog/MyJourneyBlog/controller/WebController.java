package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.model.ProjectUpdate;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import com.myjourneyblog.MyJourneyBlog.service.ProjectUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/create-update")
    public String createUpdate() {
        return "create-update";
    }

    @GetMapping("/learning/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", learningPostService.getPostById(id));
        return "post-detail";
    }

    /**
     * Learning Posts by Date (With Pagination)
     */
    @GetMapping("/learning/by-date")
    public String learningByDate(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 5; // Number of posts per page

        // 1. Get a PAGE of posts (sorted by newest first)
        Page<LearningPostDTO> postPage = learningPostService.getAllPosts(page, pageSize, "createdAt", "DESC");

        // 2. Group THIS PAGE's posts by Date
        Map<LocalDate, List<LearningPostDTO>> postsByDate = postPage.getContent().stream()
                .filter(post -> post.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        post -> post.getCreatedAt().toLocalDate(),
                        () -> new TreeMap<LocalDate, List<LearningPostDTO>>(Comparator.reverseOrder()),
                        Collectors.toList()
                ));

        // 3. Add to model with the name expected by the Thymeleaf template
        model.addAttribute("postsByDate", postsByDate);
        model.addAttribute("activePage", "by-date");

        // 4. Add pagination info
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        return "learning-by-date";
    }

    /**
     * Project Updates by Date Page (With Pagination)
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

    @GetMapping("/projects/by-project")
    public String projectsByName(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 5; // Number of projects per page

        // 1. Get all projects by name
        Page<ProjectUpdateDTO> projectsPage = projectUpdateService.getAllProject(page, pageSize, "createdAt", "DESC");

        // 2. Group THIS PAGE's projects by Name
        Map<LocalDate, List<ProjectUpdateDTO>> projectsByName = projectsPage.stream()
                .filter(update -> update.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        update -> update.getCreatedAt().toLocalDate(),
                        () -> new TreeMap<LocalDate, List<ProjectUpdateDTO>>(Comparator.reverseOrder()),
                        Collectors.toList()
                ));

        // 3. Add to model with the name expected by the Thymeleaf template
        model.addAttribute("projectsByName", projectsByName);
        model.addAttribute("activePage", "by-date");

        // 4. Add pagination info
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", projectsPage.getTotalPages());
        return "projects-by-project";
    }

    @GetMapping("/test")
    String index(Principal principal) {
        return principal != null ? "homeSignedIn" : "homeNotSignedIn";
    }
}
