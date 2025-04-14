package ru.tms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.tms.dto.Project;
import ru.tms.mappers.ProjectMapper;
import ru.tms.services.ProjectServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/api")
@Tag(name = "Project", description = "the Project in TMS API")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;
    private final ProjectMapper projectMapper;

    public ProjectController(ProjectServiceImpl projectServiceImpl, ProjectMapper projectMapper) {
        this.projectServiceImpl = projectServiceImpl;
        this.projectMapper = projectMapper;
    }

    @Operation(summary = "Get all Projects", description = "Return all Projects", tags = {"Project"})
    @GetMapping(value = "/projects")
    public Collection<Project> getAllProjects() {
        return projectServiceImpl.getAllProjects();
    }

    @Operation(summary = "Add Project", description = "Return created Project", tags = {"Project"})
    @PostMapping(value = "/project", consumes = "application/json")
    public Project createProject(@RequestBody Project project) {
        return projectServiceImpl.createProject(project);
    }

    @Operation(summary = "Update Project", description = "Return updated Project", tags = {"Project"})
    @PutMapping(value = "/project/{projectId}", consumes = "application/json")
    public Project updateProject(@PathVariable Long projectId, @RequestBody Project project) {
        return projectServiceImpl.updateProject(projectId, project);
    }

    @Operation(summary = "Delete Project", description = "Deletes the Project", tags = {"Project"})
    @DeleteMapping(value = "/project/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectServiceImpl.deleteProject(projectId);
    }

    @Operation(summary = "Get Project by Id", description = "Return Project", tags = {"Project"})
    @GetMapping(value = "/project/{projectId}")
    public Project getProject(@PathVariable Long projectId) {
        return projectMapper.toDto(projectServiceImpl.getProjectById(projectId));
    }

}
