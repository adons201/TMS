package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.Project;

import java.util.List;

public interface ProjectClient {

    Project getProject(Long projectId);

    List<Project> getProjects();

    Project createProject(Project project);

    Project updateProject(Long projectId, Project project);

    ResponseEntity<Void> deleteProject(Long projectId);

}
