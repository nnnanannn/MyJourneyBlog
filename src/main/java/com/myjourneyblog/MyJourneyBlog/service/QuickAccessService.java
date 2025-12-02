package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import java.util.List;

public interface QuickAccessService {

    /**
     * Toggles the quick access status for a post globally.
     * @return true if added, false if removed
     */
    boolean toggleQuickAccess(Long postId);

    /**
     * Get all quick access posts (visible to everyone)
     */
    List<LearningPostDTO> getQuickAccessList();
}