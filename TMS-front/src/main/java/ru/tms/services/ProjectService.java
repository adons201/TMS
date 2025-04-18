package ru.tms.services;


import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import ru.tms.dto.Project;
import ru.tms.services.client.ProjectClient;

import java.util.List;

@RequiredArgsConstructor
public class ProjectService implements ProjectClient {

    private final RestClient restClient;

    @Override
    public List<Project> getProjects() {
        return this.restClient.get()
                .uri("/api/projects")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Project>>() {
                });
    }

    @Override
    public Project getProject(Long projectId) {
        return this.restClient.get()
                .uri("/api/project/{projectId}", projectId)
                .retrieve()
                .body(Project.class);
    }

    @Override
    public Project createProject(Project project) {
        return this.restClient.post()
                .uri("/api/project")
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .retrieve()
                .body(Project.class);
    }

    @Override
    public Project updateProject(Long projectId, Project project) {
        return this.restClient.put()
                .uri("/api/project/{projectId}", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(project)
                .retrieve()
                .body(Project.class);
    }

    @Override
    public ResponseEntity<Void> deleteProject(Long projectId) {
        return this.restClient.delete()
                .uri("/api/project/{projectId}", projectId)
                .retrieve()
                .toBodilessEntity();
    }
}