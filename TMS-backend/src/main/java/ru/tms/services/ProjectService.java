package ru.tms.services;

import ru.tms.dto.ProjectDto;
import ru.tms.entity.Project;

import java.util.List;

public interface ProjectService {

    List<ProjectDto> getAllProjects();

    Project getProjectById(Long id);

    ProjectDto createProject(ProjectDto project);

    ProjectDto updateProject(Long id, ProjectDto projectDto);

    void deleteProject(Long id);
}
