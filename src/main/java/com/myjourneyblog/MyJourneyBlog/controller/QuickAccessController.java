package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.service.QuickAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quick-access")
@RequiredArgsConstructor
@Tag(name = "Quick Access", description = "Manage Pinned/Favorite posts")
public class QuickAccessController {

    private final QuickAccessService quickAccessService;

    // Only authenticated users (You) can change the list
    @PostMapping("/toggle/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Toggle quick access for a post")
    public ResponseEntity<Map<String, Object>> toggleQuickAccess(@PathVariable Long postId) {

        boolean isAdded = quickAccessService.toggleQuickAccess(postId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "isSaved", isAdded,
                "message", isAdded ? "Added to Quick Access" : "Removed from Quick Access"
        ));
    }

    // Public: Guests can see what you pinned
    @GetMapping
    @Operation(summary = "Get global quick access list")
    public ResponseEntity<List<LearningPostDTO>> getQuickAccessList() {
        return ResponseEntity.ok(quickAccessService.getQuickAccessList());
    }
}