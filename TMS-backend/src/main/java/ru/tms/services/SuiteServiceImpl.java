package ru.tms.services;

import jakarta.transaction.Transactional;
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

    private final SuiteRepo suiteRepo;
    private final ProjectServiceImpl projectServiceImpl;
    private final ProjectMapper projectMapper;

    public SuiteService(SuiteRepo suiteRepo, ProjectServiceImpl projectServiceImpl, ProjectMapper projectMapper) {
        this.suiteRepo = suiteRepo;
        this.projectServiceImpl = projectServiceImpl;
        this.projectMapper = projectMapper;
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
        ProjectDto project = projectServiceImpl.getProjectById(suiteDto.projectId());
        Suite parentSuite = null;
        if (suiteDto.parentId() != null) {
            parentSuite = getSuiteById(suiteDto.parentId());
        }
        Suite suite = new Suite();
        suite.setName(suiteDto.name());
        suite.setDescription(suiteDto.description());
        suite.setProject(projectMapper.toEntity(project));
        suite.setParentId(parentSuite);
        return suiteRepo.save(suite);
    }

    @Transactional
    public synchronized Suite updateSuite(Long suiteId, SuiteDto suiteDto) {
        Suite suite = getSuiteById(suiteId);
        ProjectDto project = projectServiceImpl.getProjectById(suiteDto.projectId());
        Suite parentSuite = null;
        if (suiteDto.parentId() != null) {
            parentSuite = getSuiteById(suiteDto.parentId());
        }
        suite.setName(suiteDto.name());
        suite.setDescription(suiteDto.description());
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
