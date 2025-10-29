package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;

public interface ProjectUpdateService {

    ProjectUpdateDTO getUpdateDescription();

    ProjectUpdateDTO getUpdateType();

    ProjectUpdateDTO getProjectStatus();

    void createUpdateProject();

    void deleteUpdateProject();

    ProjectUpdateDTO getLessonsLearned();

    ProjectUpdateDTO getTechnologiesUsed();
}
