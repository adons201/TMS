package ru.tms.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tms.entity.Suite;
import ru.tms.entity.Test;
import ru.tms.dto.ProjectDto;
import ru.tms.dto.TestDto;
import ru.tms.mappers.ProjectMapper;
import ru.tms.repo.TestRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TestService {

    private final TestRepo testRepo;
    private final ProjectService projectService;
    private final SuiteService suiteService;
    private final ProjectMapper projectMapper;

    public TestService(TestRepo testRepo, ProjectServiceImpl projectServiceImpl
    , SuiteService suiteService, ProjectMapper projectMapper) {
        this.testRepo = testRepo;
        this.projectService = projectServiceImpl;
        this.suiteService = suiteService;
        this.projectMapper = projectMapper;
    }

    public Test getTestById(Long id) throws NoSuchElementException {
        return testRepo.findById(id).get();
    }

    public List<Test> getAllTests() {
        return testRepo.findAll();
    }

    public List<Test> getAllByProjectId(Long id) {
        return testRepo.findAllTestsByProject(id);
    }

    public List<Test> getAllBySuiteId(Long id) {
        return testRepo.findAllTestsBySuite(id);
    }

    public List<Test> getAllAndChildBySuiteId(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        suiteService.getAllChildSuitesBySuite(id).stream().forEach(x -> ids.add(x.getId()));
        return testRepo.findAllTestsBySuites(ids);
    }

    @Transactional
    public synchronized Test insert(TestDto testDto) {
        Test test = new Test();
        Suite suite = null;
        if (testDto.suiteId() != null) {
            suite = suiteService.getSuiteById(testDto.suiteId());
        }
        test.setTitle(testDto.title());
        test.setProjectId(projectMapper.toEntity(projectService.getProjectById(testDto.projectId())));
        test.setSuiteId(suite);
        test.setDescription(testDto.description());
        test.setStatus(testDto.status());
        test.setSteps(testDto.steps());
        test.setAutomated(testDto.automated());
        testRepo.save(test);
        return test;
    }

    @Transactional
    public synchronized void del(Long id) {
        Test test = getTestById(id);
        testRepo.delete(test);
    }

    @Transactional
    public synchronized Test updateTest(Long testId, TestDto testDto) {
        Test test = getTestById(testId);
        Suite suite = null;
        if (testDto.suiteId() != null) {
            suite = suiteService.getSuiteById(testDto.suiteId());
        }
        test.setTitle(testDto.title());
        test.setProjectId(projectMapper.toEntity(projectService.getProjectById(testDto.projectId())));
        test.setSuiteId(suite);
        test.setDescription(testDto.description());
        test.setStatus(testDto.status());
        test.setSteps(testDto.steps());
        test.setAutomated(testDto.automated());
        testRepo.save(test);
        return test;
    }
}