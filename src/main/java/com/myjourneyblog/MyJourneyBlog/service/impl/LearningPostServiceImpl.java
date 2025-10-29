package com.myjourneyblog.MyJourneyBlog.service.impl;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.model.LearningPost;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.LearningPostRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.service.LearningPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LearningPostServiceImpl implements LearningPostService {

    private final LearningPostRepository learningPostRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public LearningPostDTO createPost(Long authorId, LearningPostDTO postDTO) {
        log.info("Creating learning post for author ID: {}", authorId);

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", authorId));

        LearningPost post = LearningPost.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .keyTakeaways(postDTO.getKeyTakeaways())
                .category(postDTO.getCategory())
                .resourcesUsed(postDTO.getResourcesUsed())
                .author(author)
                .build();

        LearningPost savedPost = learningPostRepository.save(post);
        log.info("Learning post created with ID: {}", savedPost.getId());

        return toDTO(savedPost);
    }

    @Override
    public LearningPostDTO getPostById(Long id) {
        log.debug("Fetching learning post by ID: {}", id);

        LearningPost post = learningPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LearningPost", id));

        return toDTO(post);
    }

    @Override
    public List<LearningPostDTO> getAllPosts() {
        log.debug("Fetching all learning posts");

        return learningPostRepository.findAllWithAuthors()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningPostDTO> getPostsByAuthor(Long authorId) {
        log.debug("Fetching posts by author ID: {}", authorId);

        return learningPostRepository.findByAuthorIdWithAuthor(authorId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LearningPostDTO> getPostsByCategory(String category) {
        return learningPostRepository.findByCategoryWithAuthor(category)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LearningPostDTO updatePost(Long id, LearningPostDTO postDTO) {
        log.info("Updating learning post ID: {}", id);

        LearningPost post = learningPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LearningPost", id));

        if (postDTO.getTitle() != null) {
            post.setTitle(postDTO.getTitle());
        }
        if (postDTO.getContent() != null) {
            post.setContent(postDTO.getContent());
        }
        if (postDTO.getKeyTakeaways() != null) {
            post.setKeyTakeaways(postDTO.getKeyTakeaways());
        }
        if (postDTO.getCategory() != null) {
            post.setCategory(postDTO.getCategory());
        }
        if (postDTO.getResourcesUsed() != null) {
            post.setResourcesUsed(postDTO.getResourcesUsed());
        }

        LearningPost updatedPost = learningPostRepository.save(post);
        log.info("Learning post updated: {}", updatedPost.getId());

        return toDTO(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        log.info("Deleting learning post ID: {}", id);

        if (!learningPostRepository.existsById(id)) {
            throw new ResourceNotFoundException("LearningPost", id);
        }

        learningPostRepository.deleteById(id);
        log.info("Learning post deleted: {}", id);
    }

    @Override
    public List<LearningPostDTO> searchPosts(String keyword) {
        return learningPostRepository.searchByTitleOrContent(keyword)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private LearningPostDTO toDTO(LearningPost post) {
        return LearningPostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .keyTakeaways(post.getKeyTakeaways())
                .category(post.getCategory())
                .resourcesUsed(post.getResourcesUsed())
                .authorId(post.getAuthor().getId())
                .authorUsername(post.getAuthor().getUsername())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
