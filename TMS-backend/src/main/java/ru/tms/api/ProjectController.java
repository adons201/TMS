package ru.tms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.tms.dto.ProjectDto;
import ru.tms.services.ProjectService;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@Tag(name = "Project", description = "the Project in TMS API")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Get all Projects", description = "Return all Projects", tags = {"Project"})
    @GetMapping(value = "/projects")
    public Collection<ProjectDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Add Project", description = "Return created Project", tags = {"Project"})
    @PostMapping(value = "/project", consumes = "application/json")
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        return projectService.createProject(projectDto);
    }

    @Operation(summary = "Update Project", description = "Return updated Project", tags = {"Project"})
    @PutMapping(value = "/project/{projectId}", consumes = "application/json")
    public ProjectDto updateProject(@PathVariable Long projectId, @RequestBody ProjectDto projectDto) {
        return projectService.updateProject(projectId, projectDto);
    }

    @Operation(summary = "Delete Project", description = "Deletes the Project", tags = {"Project"})
    @DeleteMapping(value = "/project/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }

    @Operation(summary = "Get Project by Id", description = "Return Project", tags = {"Project"})
    @GetMapping(value = "/project/{projectId}")
    public ProjectDto getProject(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId);
    }

}
