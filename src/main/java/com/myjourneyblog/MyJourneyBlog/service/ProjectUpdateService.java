package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;

public interface ProjectUpdateService {

    ProjectUpdateDTO getProjectName();

    ProjectUpdateDTO getUpdateDescription();

    ProjectUpdateDTO getUpdateType();

    ProjectUpdateDTO getProjectStatus();

    ProjectUpdateDTO updateProject();

    void createUpdateProject();
}
