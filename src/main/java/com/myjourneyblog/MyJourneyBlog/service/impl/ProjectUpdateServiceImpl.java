package com.myjourneyblog.MyJourneyBlog.service.impl;

import com.myjourneyblog.MyJourneyBlog.dto.ProjectUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.model.*;
import com.myjourneyblog.MyJourneyBlog.repository.ProjectUpdateRepository;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.service.ProjectUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .projectName(updateDTO.getProjectName())
                .updateType(updateDTO.getUpdateType())
                .projectStatus(updateDTO.getProjectStatus())
                .githubRepoUrl(updateDTO.getGithubRepoUrl())
                .technologiesUsed(updateDTO.getTechnologiesUsed())
                .challengesFaced(updateDTO.getChallengesFaced())
                .lessonsLearned(updateDTO.getLessonsLearned())
                .nextSteps(updateDTO.getNextSteps())
                .author(author)
                .build();

        ProjectUpdate savedProjectUpdate = projectUpdateRepository.save(projectUpdate);
        log.info("Project updated created with ID: {}", savedProjectUpdate.getId());

        return toDTO(savedProjectUpdate);
    }

    @Override
    public List<ProjectUpdateDTO> getUpdatesByProject(String projectName) {
        log.debug("Fetching project update by project name: {}", projectName);
        return projectUpdateRepository.findByProjectName(projectName)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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

    @Override
    public List<ProjectUpdateDTO> getUpdatesGroupedByProject() {
        return List.of();
    }

    @Override
    public Page<ProjectUpdateDTO> getAllProject(int page, int size, String sortBy, String direction) {
        // Deprecated/Legacy support if needed, or you can remove
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return projectUpdateRepository.findAllWithAuthors(pageable).map(this::toDTO);
    }

//    @Override
//    public Page<ProjectUpdateDTO> getAllProject(int page, int size, String sortBy, String direction) {
//        log.debug("Fetching all projects from database - page: {}, size: {}", page, size);
//
//        Sort sort = direction.equalsIgnoreCase("DESC")
//                ? Sort.by(sortBy).descending()
//                : Sort.by(sortBy).ascending();
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//        Page<ProjectUpdate> projectPage = projectUpdateRepository.findByProjectName(projectName);
//
//        return projectPage.map(this::toDTO);
//    }

    @Override
    public Page<ProjectUpdateDTO> getDistinctProjects(Pageable pageable) {
        log.debug("Fetching distinct projects (latest update per project)");
        return projectUpdateRepository.findDistinctProjects(pageable)
                .map(this::toDTO);
    }

    @Override
    public Page<ProjectUpdateDTO> getProjectUpdates(String projectName, Pageable pageable) {
        log.debug("Fetching paginated updates for project: {}", projectName);

        // Use the repository method that filters by Name
        Page<ProjectUpdate> updates = projectUpdateRepository.findByProjectName(projectName, pageable);

        return updates.map(this::toDTO);
    }

    @Override
    public ProjectUpdateDTO getUpdateById(Long id) {
        log.debug("Fetching project update by ID: {}", id);

        ProjectUpdate update = projectUpdateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project update", id));

        return toDTO(update);
    }

    @Override
    @Transactional
    public void deleteProject(String projectName) {
        log.info("Deleting all updates for project: {}", projectName);
        projectUpdateRepository.deleteByProjectName(projectName);
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
        return projectUpdateRepository.searchByTitleOrDescription(keyword)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectUpdateDTO updateUpdate(Long id, ProjectUpdateDTO updateDTO) {
        log.info("Updating project update ID: {}", id);

        ProjectUpdate update = projectUpdateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectUpdate", id));

        if (updateDTO.getTitle() != null) update.setTitle(updateDTO.getTitle());
        if (updateDTO.getDescription() != null) update.setDescription(updateDTO.getDescription());
        if (updateDTO.getProjectName() != null) update.setProjectName(updateDTO.getProjectName());
        if (updateDTO.getProjectStatus() != null) update.setProjectStatus(updateDTO.getProjectStatus());
        if (updateDTO.getUpdateType() != null) update.setUpdateType(updateDTO.getUpdateType());
        if (updateDTO.getTechnologiesUsed() != null) update.setTechnologiesUsed(updateDTO.getTechnologiesUsed());
        if (updateDTO.getGithubRepoUrl() != null) update.setGithubRepoUrl(updateDTO.getGithubRepoUrl());

        ProjectUpdate saved = projectUpdateRepository.save(update);
        return toDTO(saved);
    }

    private ProjectUpdateDTO toDTO(ProjectUpdate projectUpdate) {
        return ProjectUpdateDTO.builder()
                .id(projectUpdate.getId())
                .title(projectUpdate.getTitle())
                .description(projectUpdate.getDescription())
                .projectName(projectUpdate.getProjectName())
                .updateType(projectUpdate.getUpdateType())
                .projectStatus(projectUpdate.getProjectStatus())
                .githubRepoUrl(projectUpdate.getGithubRepoUrl())
                .technologiesUsed(projectUpdate.getTechnologiesUsed())
                .challengesFaced(projectUpdate.getChallengesFaced())
                .lessonsLearned(projectUpdate.getLessonsLearned())
                .nextSteps(projectUpdate.getNextSteps())
                .createdAt(projectUpdate.getCreatedAt())
                .updatedAt(projectUpdate.getUpdatedAt())
                .authorId(projectUpdate.getAuthor().getId())
                .authorUsername(projectUpdate.getAuthor().getUsername())
                .build();
    }
}
