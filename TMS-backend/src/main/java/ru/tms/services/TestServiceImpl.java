package ru.tms.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.tms.entity.Suite;
import ru.tms.entity.Test;
import ru.tms.dto.TestDto;
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

    public Test getTestById(Long id) throws NoSuchElementException {
        return this.testRepo.findById(id).get();
    }

    public List<TestDto> getAllTests() {
        return testMapper.toDto(this.testRepo.findAll());
    }

    public List<TestDto> getAllTestsByProjectId(Long id) {
        return testMapper.toDto(this.testRepo.findAllTestsByProject(id));
    }

    public List<TestDto> getAllTestsBySuiteId(Long id) {
        return testMapper.toDto(this.testRepo.findAllTestsBySuite(id));
    }

    public List<TestDto> getAllAndChildBySuiteId(Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        this.suiteServiceImpl.getAllChildSuitesBySuite(id).stream().forEach(x -> ids.add(x.id()));
        return testMapper.toDto(this.testRepo.findAllTestsBySuites(ids));
    }

    @Transactional
    public synchronized TestDto createTest(TestDto testDto) {
        Test test = new Test();
        Suite suite = null;
        if (testDto.suiteId() != null) {
            suite = this.suiteServiceImpl.getSuiteById(testDto.suiteId());
        }
        test.setTitle(testDto.title());
        test.setProjectId(this.projectService.getProjectById(testDto.projectId()));
        test.setSuiteId(suite);
        test.setDescription(testDto.description());
        test.setStatus(testDto.status());
        test.setSteps(testDto.steps());
        test.setAutomated(testDto.automated());
        this.testRepo.save(test);
        return testMapper.toDto(test);
    }

    @Transactional
    public synchronized void deleteTest(Long id) {
        Test test = getTestById(id);
        this.testRepo.delete(test);
    }

    @Transactional
    public synchronized TestDto updateTest(Long testId, TestDto testDto) {
        Test test = getTestById(testId);
        Suite suite = null;
        if (testDto.suiteId() != null) {
            suite = this.suiteServiceImpl.getSuiteById(testDto.suiteId());
        }
        test.setTitle(testDto.title());
        test.setProjectId(this.projectService.getProjectById(testDto.projectId()));
        test.setSuiteId(suite);
        test.setDescription(testDto.description());
        test.setStatus(testDto.status());
        test.setSteps(testDto.steps());
        test.setAutomated(testDto.automated());
        this.testRepo.save(test);
        return testMapper.toDto(test);
    }
}