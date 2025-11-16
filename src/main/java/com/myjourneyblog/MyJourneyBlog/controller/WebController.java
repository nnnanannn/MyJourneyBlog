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
import java.util.List;
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
                //.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());

        // Get recent project updates (latest 5, sorted by creation date)
        List<ProjectUpdateDTO> recentUpdates = projectUpdateService.getAllUpdates()
                .stream()
                //.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());

        // Add data to model for Thymeleaf to use
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("recentUpdates", recentUpdates);
        model.addAttribute("activePage", "home");

        //model.addAttribute("recentPosts", learningPostService.getAllPosts());
        //model.addAttribute("recentUpdates", projectUpdateService.getAllUpdates());


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
        List<LearningPostDTO> recentPosts = learningPostService.getAllPosts()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());

        // Add data to model for Thymeleaf to use
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("activePage", "by-date");

        return "learning-by-date";
    }

    @GetMapping("/test")
    String index(Principal principal) {
        return principal != null ? "homeSignedIn" : "homeNotSignedIn";
    }
}
