package ru.tms.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.tms.dto.ProjectDto;
import ru.tms.mappers.ProjectMapper;
import ru.tms.repo.ProjectRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepo projectRepo;

    public ProjectService(ProjectRepo projectRepo, ProjectMapper projectMapper) {
        this.projectRepo = projectRepo;
        this.projectMapper = projectMapper;
    }

    public List<ProjectDto> getAllProjects() {
        return projectMapper.toDto(projectRepo.findAll());
    }
    public ProjectDto getProjectById(Long id) throws NoSuchElementException {
        return projectMapper.toDto(projectRepo.findById(id).get());
    }

    @Transactional
    public ProjectDto createProject(ProjectDto projectDto) {
        return projectMapper.toDto((projectRepo.save(projectMapper.toEntity(projectDto))));
    }
    @Transactional
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        return projectMapper.toDto(projectRepo.save(projectMapper.toEntity(projectDto)));
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepo.deleteById(id);
    }

}
