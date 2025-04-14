package ru.tms.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.tms.dto.Project;
import ru.tms.entity.ProjectEntity;
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
    public List<Project> getAllProjects() {
        return projectMapper.toDto(projectRepo.findAll());
    }

    @Override
    public ProjectEntity getProjectById(Long id) throws NoSuchElementException {
        return projectRepo.findById(id).get();
    }

    @Override
    @Transactional
    public Project createProject(Project project) {
        return projectMapper.toDto(projectRepo.save(this.projectMapper.toEntity(project)));
    }

    @Override
    @Transactional
    public Project updateProject(Long id, Project project) {
        return projectMapper.toDto(projectRepo.save(this.projectMapper.toEntity(project)));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        projectRepo.deleteById(id);
    }

}
