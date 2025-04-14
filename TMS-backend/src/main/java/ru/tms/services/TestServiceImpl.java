package ru.tms.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.tms.entity.SuiteEntity;
import ru.tms.entity.TestEntity;
import ru.tms.dto.Test;
import ru.tms.mappers.TestMapper;
import ru.tms.repo.TestRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TestServiceImpl implements TestService{

    private final TestRepo testRepo;
    private final ProjectService projectService;
    private final SuiteServiceImpl suiteServiceImpl;
    private final TestMapper testMapper;

    public TestServiceImpl(TestRepo testRepo, ProjectServiceImpl projectServiceImpl
    , SuiteServiceImpl suiteServiceImpl, TestMapper testMapper) {
        this.testRepo = testRepo;
        this.projectService = projectServiceImpl;
        this.suiteServiceImpl = suiteServiceImpl;
        this.testMapper = testMapper;
    }

    public TestEntity getTestById(Long id) throws NoSuchElementException {
        return this.testRepo.findById(id).get();
    }

    public List<Test> getAllTests() {
        return testMapper.toDto(this.testRepo.findAll());
    }

    public List<Test> getAllTestsByProjectId(Long id) {
        return testMapper.toDto(this.testRepo.findAllTestsByProject(id));
    }

    public List<Test> getAllTestsBySuiteId(Long id) {
        return testMapper.toDto(this.testRepo.findAllTestsBySuite(id));
    }

    public List<Test> getAllAndChildBySuiteId(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        this.suiteServiceImpl.getAllChildSuitesBySuite(id).stream().forEach(x -> ids.add(x.id()));
        return testMapper.toDto(this.testRepo.findAllTestsBySuites(ids));
    }

    @Transactional
    public synchronized Test createTest(Test test) {
        TestEntity testEntity = new TestEntity();
        SuiteEntity suiteEntity = null;
        if (test.suiteId() != null) {
            suiteEntity = this.suiteServiceImpl.getSuiteById(test.suiteId());
        }
        testEntity.setTitle(test.title());
        testEntity.setProjectId(this.projectService.getProjectById(test.projectId()));
        testEntity.setSuiteId(suiteEntity);
        testEntity.setDescription(test.description());
        testEntity.setStatus(test.status());
        testEntity.setSteps(test.steps());
        testEntity.setAutomated(test.automated());
        this.testRepo.save(testEntity);
        return testMapper.toDto(testEntity);
    }

    @Transactional
    public synchronized void deleteTest(Long id) {
        TestEntity testEntity = getTestById(id);
        this.testRepo.delete(testEntity);
    }

    @Transactional
    public synchronized Test updateTest(Long testId, Test test) {
        TestEntity testEntity = getTestById(testId);
        SuiteEntity suiteEntity = null;
        if (test.suiteId() != null) {
            suiteEntity = this.suiteServiceImpl.getSuiteById(test.suiteId());
        }
        testEntity.setTitle(test.title());
        testEntity.setProjectId(this.projectService.getProjectById(test.projectId()));
        testEntity.setSuiteId(suiteEntity);
        testEntity.setDescription(test.description());
        testEntity.setStatus(test.status());
        testEntity.setSteps(test.steps());
        testEntity.setAutomated(test.automated());
        this.testRepo.save(testEntity);
        return testMapper.toDto(testEntity);
    }
}