package ru.tms.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.tms.dto.SuiteDto;
import ru.tms.entity.Suite;
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

    public List<SuiteDto> getAllSuitesByProject(Long projectId) {
        return this.suiteMapper.toDto(this.suiteRepo.findAllSuiteByProject(projectId));
    }

    public List<SuiteDto> getAllChildSuitesBySuite(Long suiteId) {
        return this.suiteMapper.toDto(this.suiteRepo.findAllChildSuitesBySuite(suiteId));
    }

    public List<SuiteDto> getChildSuitesBySuite(Long suiteId) {
        return this.suiteMapper.toDto(this.suiteRepo.findChildSuitesBySuite(suiteId));
    }

    public Suite getSuiteById(Long suiteId) throws NoSuchElementException {
        return this.suiteRepo.findById(suiteId).get();
    }

    @Transactional
    public synchronized SuiteDto createSuite(SuiteDto suiteDto) {
        Suite parentSuite = null;
        if (suiteDto.parentId() != null) {
            parentSuite = getSuiteById(suiteDto.parentId());
        }
        Suite suite = new Suite();
        suite.setName(suiteDto.name());
        suite.setDescription(suiteDto.description());
        suite.setProject(this.projectServiceImpl.getProjectById(suiteDto.projectId()));
        suite.setParentId(parentSuite);
        return this.suiteMapper.toDto(this.suiteRepo.save(suite));
    }

    @Transactional
    public synchronized SuiteDto updateSuite(Long suiteId, SuiteDto suiteDto) {
        Suite suite = getSuiteById(suiteId);
        Suite parentSuite = null;
        if (suiteDto.parentId() != null) {
            parentSuite = getSuiteById(suiteDto.parentId());
        }
        suite.setName(suiteDto.name());
        suite.setDescription(suiteDto.description());
        suite.setProject(this.projectServiceImpl.getProjectById(suiteDto.projectId()));
        suite.setParentId(parentSuite);
        return this.suiteMapper.toDto(this.suiteRepo.save(suite));
    }

    @Transactional
    public synchronized void deleteSuite(Long suiteId) {
        Suite suite = getSuiteById(suiteId);
        this.suiteRepo.delete(suite);
    }
}
