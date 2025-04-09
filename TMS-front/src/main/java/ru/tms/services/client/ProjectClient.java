package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.ProjectDto;

import java.util.List;

public interface ProjectClient {

    ProjectDto getProject(Long projectId);

    List<ProjectDto> getProjects();

    ProjectDto createProject(ProjectDto projectDto);

    ProjectDto updateProject(Long projectId, ProjectDto projectDto);

    ResponseEntity<Void> deleteProject(Long projectId);

}
