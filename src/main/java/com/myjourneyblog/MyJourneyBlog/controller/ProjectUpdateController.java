package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import com.myjourneyblog.MyJourneyBlog.security.UserPrincipal;
import com.myjourneyblog.MyJourneyBlog.service.ProjectUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for ProjectUpdate operations
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectUpdateController {

    private final ProjectUpdateService projectUpdateService;

    /**
     * Create new project update
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjectUpdateDTO> createUpdate(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody ProjectUpdateDTO updateDTO) {

        ProjectUpdateDTO createdUpdate = projectUpdateService.createUpdate(
                currentUser.getId(),
                updateDTO
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUpdate);
    }

    /**
     * Get all project updates
     */
    @GetMapping
    public ResponseEntity<List<ProjectUpdateDTO>> getAllUpdates() {
        List<ProjectUpdateDTO> updates = projectUpdateService.getAllUpdates();
        return ResponseEntity.ok(updates);
    }

    /**
     * Get update by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectUpdateDTO> getUpdateById(@PathVariable Long id) {
        ProjectUpdateDTO update = projectUpdateService.getUpdateById(id);
        return ResponseEntity.ok(update);
    }

    /**
     * Get current user's project updates
     */
    @GetMapping("/my-updates")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProjectUpdateDTO>> getMyUpdates(
            @AuthenticationPrincipal UserPrincipal currentUser) {

        List<ProjectUpdateDTO> updates = projectUpdateService.getUpdatesByAuthor(
                currentUser.getId()
        );

        return ResponseEntity.ok(updates);
    }

    /**
     * Get updates by author
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<ProjectUpdateDTO>> getUpdatesByAuthor(
            @PathVariable Long authorId) {

        List<ProjectUpdateDTO> updates = projectUpdateService.getUpdatesByAuthor(authorId);
        return ResponseEntity.ok(updates);
    }

    /**
     * Get updates by project status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectUpdateDTO>> getUpdatesByStatus(
            @PathVariable ProjectStatus status) {

        List<ProjectUpdateDTO> updates = projectUpdateService.getUpdatesByStatus(status);
        return ResponseEntity.ok(updates);
    }

    /**
     * Get updates by update type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProjectUpdateDTO>> getUpdatesByType(
            @PathVariable UpdateType type) {

        List<ProjectUpdateDTO> updates = projectUpdateService.getUpdatesByType(type);
        return ResponseEntity.ok(updates);
    }

    /**
     * Get updates by project name
     */
    @GetMapping("/project/{projectName}")
    public ResponseEntity<List<ProjectUpdateDTO>> getUpdatesByProject(
            @PathVariable String projectName) {

        List<ProjectUpdateDTO> updates = projectUpdateService.getUpdatesByProject(projectName);
        return ResponseEntity.ok(updates);
    }

    /**
     * Search updates
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProjectUpdateDTO>> searchUpdates(
            @RequestParam String keyword) {

        List<ProjectUpdateDTO> updates = projectUpdateService.searchUpdates(keyword);
        return ResponseEntity.ok(updates);
    }

    /**
     * Update project update
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjectUpdateDTO> updateProjectUpdate(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateDTO updateDTO) {

        ProjectUpdateDTO updated = projectUpdateService.updateUpdate(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete project by name
     */
    @DeleteMapping("/project/{projectName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectName) {
        projectUpdateService.deleteProject(projectName);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete project update
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteUpdate(@PathVariable Long id) {
        projectUpdateService.deleteUpdate(id);
        return ResponseEntity.noContent().build();
    }
}
