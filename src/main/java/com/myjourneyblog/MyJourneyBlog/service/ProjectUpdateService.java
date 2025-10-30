package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.LearningPostDTO;
import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectUpdateService {

    ProjectUpdateDTO createProjectUpdate(Long authorId, ProjectUpdateDTO projectUpdateDTO);

    ProjectUpdateDTO getProjectUpdateByProjectName(String projectName);

    ProjectUpdateDTO getProjectUpdateById(Long id);

    List<ProjectUpdateDTO> getAllProjectUpdates();

    List<ProjectUpdateDTO> getProjectUpdateByType(UpdateType updateType);

    List<ProjectUpdateDTO> getProjectUpdateByStatus(ProjectStatus projectStatus);

    void deleteUpdateProject(Long id);
}
