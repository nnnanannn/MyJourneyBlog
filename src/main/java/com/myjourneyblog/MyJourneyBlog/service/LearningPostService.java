package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;

import java.util.List;

public interface LearningPostService {

    LearningPostDTO createPost(Long authorId, LearningPostDTO postDTO);

    LearningPostDTO getPostById(Long id);

    List<LearningPostDTO> getAllPosts();

    List<LearningPostDTO> getPostsByAuthor(Long authorId);

    List<LearningPostDTO> getPostsByCategory(String category);

    LearningPostDTO updatePost(Long id, LearningPostDTO postDTO);

    void deletePost(Long id);

    List<LearningPostDTO> searchPosts(String keyword);
}
