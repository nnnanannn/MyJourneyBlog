package com.myjourneyblog.MyJourneyBlog.service.impl;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.model.QuickAccess;
import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import com.myjourneyblog.MyJourneyBlog.repository.QuickAccessRepository;
import com.myjourneyblog.MyJourneyBlog.service.QuickAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QuickAccessServiceImpl implements QuickAccessService {

    private final QuickAccessRepository quickAccessRepository;
    private final LearningPostRepository learningPostRepository;

    @Override
    @Transactional
    public boolean toggleQuickAccess(Long postId) {
        // Check if already exists
        Optional<QuickAccess> existing = quickAccessRepository.findByPostId(postId);

        if (existing.isPresent()) {
            // Remove it
            quickAccessRepository.delete(existing.get());
            log.info("Removed post {} from Quick Access", postId);
            return false; // Removed
        } else {
            // Add it
            LearningPost post = learningPostRepository.findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException("Post", postId));

            QuickAccess quickAccess = QuickAccess.builder()
                    .post(post)
                    .build();

            quickAccessRepository.save(quickAccess);
            log.info("Added post {} to Quick Access", postId);
            return true; // Added
        }
    }

    @Override
    public List<LearningPostDTO> getQuickAccessList() {
        return quickAccessRepository.findAllByOrderByAddedAtDesc().stream()
                .map(qa -> toDTO(qa.getPost()))
                .collect(Collectors.toList());
    }

    private LearningPostDTO toDTO(LearningPost post) {
        return LearningPostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .authorUsername(post.getAuthor().getUsername())
                .createdAt(post.getCreatedAt())
                .learningDate(post.getLearningDate())
                .build();
    }
}