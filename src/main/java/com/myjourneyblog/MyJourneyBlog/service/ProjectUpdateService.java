package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectUpdateService {

    ProjectUpdateDTO createUpdate(Long id, @Valid ProjectUpdateDTO updateDTO);

    List<ProjectUpdateDTO> getUpdatesByProject(String projectName);
    
    ProjectUpdateDTO getUpdateById(Long id);

    List<ProjectUpdateDTO> getUpdatesByAuthor(Long authorId);

    List<ProjectUpdateDTO> getAllUpdates();

    List<ProjectUpdateDTO> getUpdatesByType(UpdateType type);

    List<ProjectUpdateDTO> getUpdatesByStatus(ProjectStatus status);

    List<ProjectUpdateDTO> getUpdatesGroupedByProject();

    Page<ProjectUpdateDTO> getAllProject(int page, int size, String sortBy, String direction);

    Page<ProjectUpdateDTO> getDistinctProjects(Pageable pageable);

    Page<ProjectUpdateDTO> getProjectUpdates(String projectName, Pageable pageable);

    void deleteUpdate(Long id);

    List<ProjectUpdateDTO> searchUpdates(String keyword);

    ProjectUpdateDTO updateUpdate(Long id, @Valid ProjectUpdateDTO updateDTO);
}
