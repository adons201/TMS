package ru.tms.services.project;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.tms.dto.ProjectDto;
import ru.tms.services.WebClientServiceBack;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ProjectService implements WebClientProject{

    private final WebClientServiceBack webClientService;

    public ProjectService(WebClientServiceBack webClientService) {
        this.webClientService = webClientService;
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return webClientService.sendRequest("/api/projects",
                WebClientServiceBack.HttpMethod.GET,
                null,
                null,
                new ParameterizedTypeReference<List<ProjectDto>>(){},
                Collections.emptyList());
    }

    @Override
    public ProjectDto getProjectById(Long projectId) throws NoSuchElementException {
        return webClientService.sendRequest("/api/project/{projectId}",
                WebClientServiceBack.HttpMethod.GET,
                Map.of("projectId", projectId),
                null,
                ProjectDto.class,
                null);
    }

    @Override
    public synchronized ProjectDto createProject(ProjectDto projectDto) {
        return webClientService.sendRequest("/api/project",
                WebClientServiceBack.HttpMethod.POST,
                null,
                projectDto,
                ProjectDto.class,
                null);
    }

    @Override
    public synchronized ProjectDto updateProject(Long projectId, ProjectDto projectDto) {
        ProjectDto project = getProjectById(projectId);
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        return webClientService.sendRequest("/api/project/{projectId}",
                WebClientServiceBack.HttpMethod.PUT,
                Map.of("projectId", projectId),
                project,
                ProjectDto.class,
                null);
    }

    @Override
    public synchronized void deleteProject(Long projectId) {
        ProjectDto project = webClientService.sendRequest("/api/project/{projectId}",
                WebClientServiceBack.HttpMethod.DELETE,
                Map.of("projectId", projectId),
                null,
                ProjectDto.class,
                null);
    }

}
