package ru.tms.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.tms.dto.Suite;
import ru.tms.entity.SuiteEntity;
import ru.tms.mappers.SuiteMapper;
import ru.tms.repo.SuiteRepo;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SuiteServiceImpl implements SuiteService{

    private final SuiteRepo suiteRepo;
    private final ProjectServiceImpl projectServiceImpl;
    private final SuiteMapper suiteMapper;

    public SuiteServiceImpl(SuiteRepo suiteRepo, ProjectServiceImpl projectServiceImpl, SuiteMapper suiteMapper) {
        this.suiteRepo = suiteRepo;
        this.projectServiceImpl = projectServiceImpl;
        this.suiteMapper = suiteMapper;
    }

    @Override
    public List<Suite> getAllSuitesByProject(Long projectId) {
        return this.suiteMapper.toDto(this.suiteRepo.findAllSuiteByProject(projectId));
    }

    @Override
    public List<Suite> getAllChildSuitesBySuite(Long suiteId) {
        return this.suiteMapper.toDto(this.suiteRepo.findAllChildSuitesBySuite(suiteId));
    }

    @Override
    public List<Suite> getChildSuitesBySuite(Long suiteId) {
        return this.suiteMapper.toDto(this.suiteRepo.findChildSuitesBySuite(suiteId));
    }

    @Override
    public SuiteEntity getSuiteById(Long suiteId) throws NoSuchElementException {
        return this.suiteRepo.findById(suiteId).get();
    }

    @Override
    @Transactional
    public synchronized Suite createSuite(Suite suite) {
        SuiteEntity parentSuiteEntity = null;
        if (suite.parentId() != null) {
            parentSuiteEntity = getSuiteById(suite.parentId());
        }
        SuiteEntity suiteEntity = new SuiteEntity();
        suiteEntity.setName(suite.name());
        suiteEntity.setDescription(suite.description());
        suiteEntity.setProject(this.projectServiceImpl.getProjectById(suite.projectId()));
        suiteEntity.setParentId(parentSuiteEntity);
        return this.suiteMapper.toDto(this.suiteRepo.save(suiteEntity));
    }

    @Override
    @Transactional
    public synchronized Suite updateSuite(Long suiteId, Suite suite) {
        SuiteEntity suiteEntity = getSuiteById(suiteId);
        SuiteEntity parentSuiteEntity = null;
        if (suite.parentId() != null) {
            parentSuiteEntity = getSuiteById(suite.parentId());
        }
        suiteEntity.setName(suite.name());
        suiteEntity.setDescription(suite.description());
        suiteEntity.setProject(this.projectServiceImpl.getProjectById(suite.projectId()));
        suiteEntity.setParentId(parentSuiteEntity);
        return this.suiteMapper.toDto(this.suiteRepo.save(suiteEntity));
    }

    @Override
    @Transactional
    public synchronized void deleteSuite(Long suiteId) {
        SuiteEntity suiteEntity = getSuiteById(suiteId);
        this.suiteRepo.delete(suiteEntity);
    }
}
