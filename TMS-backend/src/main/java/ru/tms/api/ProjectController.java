package ru.tms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.tms.dto.ProjectDto;
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
    public Collection<ProjectDto> getAllProjects() {
        return projectServiceImpl.getAllProjects();
    }

    @Operation(summary = "Add Project", description = "Return created Project", tags = {"Project"})
    @PostMapping(value = "/project", consumes = "application/json")
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        return projectServiceImpl.createProject(projectDto);
    }

    @Operation(summary = "Update Project", description = "Return updated Project", tags = {"Project"})
    @PutMapping(value = "/project/{projectId}", consumes = "application/json")
    public ProjectDto updateProject(@PathVariable Long projectId, @RequestBody ProjectDto projectDto) {
        return projectServiceImpl.updateProject(projectId, projectDto);
    }

    @Operation(summary = "Delete Project", description = "Deletes the Project", tags = {"Project"})
    @DeleteMapping(value = "/project/{projectId}")
    public void deleteProject(@PathVariable Long projectId) {
        projectServiceImpl.deleteProject(projectId);
    }

    @Operation(summary = "Get Project by Id", description = "Return Project", tags = {"Project"})
    @GetMapping(value = "/project/{projectId}")
    public ProjectDto getProject(@PathVariable Long projectId) {
        return projectMapper.toDto(projectServiceImpl.getProjectById(projectId));
    }

}
