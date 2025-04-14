package ru.tms.services;

import ru.tms.dto.Project;
import ru.tms.entity.ProjectEntity;

import java.util.List;

public interface ProjectService {

    List<Project> getAllProjects();

    ProjectEntity getProjectById(Long id);

    Project createProject(Project project);

    Project updateProject(Long id, Project project);

    void deleteProject(Long id);
}
