package ru.tms.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tms.dto.ProjectDto;
import ru.tms.dto.SuiteDto;
import ru.tms.entity.Suite;
import ru.tms.mappers.ProjectMapper;
import ru.tms.repo.SuiteRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SuiteService {

    private SuiteRepo suiteRepo;
    private ProjectService projectService;

    @Autowired
    private ProjectMapper projectMapper;

    public SuiteService(SuiteRepo suiteRepo, ProjectService projectService) {
        this.suiteRepo = suiteRepo;
        this.projectService = projectService;
    }

    public List<Suite> getAllSuitesByProject(Long projectId) {
        return suiteRepo.findAllSuiteByProject(projectId);
    }

    public List<Suite> getAllChildSuitesBySuite(Long suiteId) {
        return suiteRepo.findAllChildSuitesBySuite(suiteId);
    }

    public List<Suite> getChildSuitesBySuite(Long suiteId) {
        return suiteRepo.findChildSuitesBySuite(suiteId);
    }

    public Suite getSuiteById(Long suiteId) throws NoSuchElementException {
        return suiteRepo.findById(suiteId).get();
    }

    @Transactional
    public synchronized Suite createSuite(SuiteDto suiteDto) {
        ProjectDto project = projectService.getProjectById(suiteDto.getProjectId());
        Suite parentSuite = null;
        if (suiteDto.getParentId() != null) {
            parentSuite = getSuiteById(suiteDto.getParentId());
        }
        Suite suite = new Suite();
        suite.setName(suiteDto.getName());
        suite.setDescription(suiteDto.getDescription());
        suite.setProject(projectMapper.toEntity(project));
        suite.setParentId(parentSuite);
        return suiteRepo.save(suite);
    }

    @Transactional
    public synchronized Suite updateSuite(Long suiteId, SuiteDto suiteDto) {
        Suite suite = getSuiteById(suiteId);
        ProjectDto project = projectService.getProjectById(suiteDto.getProjectId());
        Suite parentSuite = null;
        if (suiteDto.getParentId() != null) {
            parentSuite = getSuiteById(suiteDto.getParentId());
        }
        suite.setName(suiteDto.getName());
        suite.setDescription(suiteDto.getDescription());
        suite.setProject(projectMapper.toEntity(project));
        suite.setParentId(parentSuite);
        return suiteRepo.save(suite);
    }

    @Transactional
    public synchronized void deleteSuite(Long suiteId) {
        Suite suite = getSuiteById(suiteId);
        suiteRepo.delete(suite);
    }


}
