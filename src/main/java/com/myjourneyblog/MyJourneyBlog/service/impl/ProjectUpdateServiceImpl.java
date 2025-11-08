package com.myjourneyblog.MyJourneyBlog.service.impl;

import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.model.ProjectStatus;
import com.myjourneyblog.MyJourneyBlog.model.ProjectUpdate;
import com.myjourneyblog.MyJourneyBlog.model.UpdateType;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.ProjectUpdateRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.service.ProjectUpdateService;
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
public class ProjectUpdateServiceImpl implements ProjectUpdateService {

    private final ProjectUpdateRepository projectUpdateRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectUpdateDTO createUpdate(Long authorId, ProjectUpdateDTO updateDTO) {
        log.info("Creating project update for author ID: {}", authorId);

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", authorId));

        ProjectUpdate projectUpdate = ProjectUpdate.builder()
                .title(updateDTO.getTitle())
                .description(updateDTO.getDescription())
                .updateType(updateDTO.getUpdateType())
                .projectStatus(updateDTO.getProjectStatus())
                .githubRepoUrl(updateDTO.getGithubRepoUrl())
                .technologiesUsed(updateDTO.getTechnologiesUsed())
                .challengesFaced(updateDTO.getChallengesFaced())
                .lessonsLearned(updateDTO.getLessonsLearned())
                .nextSteps(updateDTO.getNextSteps())
                .build();

        ProjectUpdate savedProjectUpdate = projectUpdateRepository.save(projectUpdate);
        log.info("Project updated created with ID: {}", savedProjectUpdate.getId());

        return toDTO(projectUpdate);
    }

    @Override
    public List<ProjectUpdateDTO> getUpdatesByProject(String projectName) {
        log.debug("Fetching project update by project name: {}", projectName);
        return null;
    }

    @Override
    public ProjectUpdateDTO getUpdateById(Long id) {
        log.debug("Fetching project update by ID: {}", id);
        return null;
    }

    @Override
    public List<ProjectUpdateDTO> getUpdatesByAuthor(Long authorId) {
        log.debug("Fetching project update by author: {}", authorId);
        return null;
    }

    @Override
    public List<ProjectUpdateDTO> getAllUpdates() {
        log.debug("Fetching all learning posts");

        return projectUpdateRepository.findAllWithAuthors()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectUpdateDTO> getUpdatesByType(UpdateType updateType) {
        log.debug("Fetching posts by update type: {}", updateType);

        return projectUpdateRepository.findByUpdateType(updateType)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectUpdateDTO> getUpdatesByStatus(ProjectStatus projectStatus) {
        log.debug("Fetching posts by project status: {}", projectStatus);

        return projectUpdateRepository.findByProjectStatus(projectStatus)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUpdate(Long id) {
        log.info("Deleting project update ID: {}", id);

        if (!projectUpdateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project update", id);
        }

        projectUpdateRepository.deleteById(id);
        log.info("Project update deleted: {}", id);

    }

    @Override
    public List<ProjectUpdateDTO> searchUpdates(String keyword) {
        return List.of();
    }

    @Override
    public ProjectUpdateDTO updateUpdate(Long id, ProjectUpdateDTO updateDTO) {
        return null;
    }

    private ProjectUpdateDTO toDTO(ProjectUpdate projectUpdate) {
        return ProjectUpdateDTO.builder()
                .id(projectUpdate.getId())
                .projectName(projectUpdate.getProjectName())
                .updateType(projectUpdate.getUpdateType())
                .projectStatus(projectUpdate.getProjectStatus())
                .githubRepoUrl(projectUpdate.getGithubRepoUrl())
                .technologiesUsed(projectUpdate.getTechnologiesUsed())
                .challengesFaced(projectUpdate.getChallengesFaced())
                .lessonsLearned(projectUpdate.getLessonsLearned())
                .nextSteps(projectUpdate.getNextSteps())
                .build();
    }
}
