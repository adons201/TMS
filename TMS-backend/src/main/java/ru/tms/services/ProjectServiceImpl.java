package ru.tms.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.tms.dto.ProjectDto;
import ru.tms.entity.Project;
import ru.tms.mappers.ProjectMapper;
import ru.tms.repo.ProjectRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectMapper projectMapper;
    private final ProjectRepo projectRepo;

    public ProjectServiceImpl(ProjectRepo projectRepo, ProjectMapper projectMapper) {
        this.projectRepo = projectRepo;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        return projectMapper.toDto(projectRepo.findAll());
    }

    @Override
    public Project getProjectById(Long id) throws NoSuchElementException {
        return projectRepo.findById(id).get();
    }

    @Override
    @Transactional
    public ProjectDto createProject(ProjectDto projectDto) {
        return projectMapper.toDto(projectRepo.save(this.projectMapper.toEntity(projectDto)));
    }

    @Override
    @Transactional
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        return projectMapper.toDto(projectRepo.save(this.projectMapper.toEntity(projectDto)));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        projectRepo.deleteById(id);
    }

}
