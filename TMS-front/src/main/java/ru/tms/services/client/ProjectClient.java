package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tms.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

public interface ProjectClient {

    ProjectDto getProject(Long projectId);

    List<ProjectDto> getProjects();

    ProjectDto createProject(ProjectDto projectDto);

    ProjectDto updateProject(Long projectId, ProjectDto projectDto);

    ResponseEntity<Void> deleteProject(Long projectId);

}
