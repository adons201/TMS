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

    private TestRepo testRepo;
    private ProjectService projectService;

    private SuiteService suiteService;

    @Autowired
    private void setSuiteService(SuiteService suiteService) {
        this.suiteService = suiteService;
    }

    @Autowired
    private ProjectMapper projectMapper;

    public TestService(TestRepo testRepo, ProjectService projectService) {
        this.testRepo = testRepo;
        this.projectService = projectService;
    }

    public Test getById(Long id) throws NoSuchElementException {
        return testRepo.findById(id).get();
    }

    public List<Test> getAll() {
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
        ProjectDto project = projectService.getProjectById(testDto.getProjectId());
        Suite suite = null;
        if (testDto.getSuiteId() != null) {
            suite = suiteService.getSuiteById(testDto.getSuiteId());
        }
        test.setTitle(testDto.getTitle());
        test.setProjectId(projectMapper.toEntity(project));
        test.setSuiteId(suite);
        test.setDescription(testDto.getDescription());
        test.setStatus(testDto.getStatus());
        test.setSteps(testDto.getSteps());
        test.setAutomated(testDto.getAutomated());
        testRepo.save(test);
        return test;
    }

    @Transactional
    public synchronized void del(Long id) {
        Test test = getById(id);
        testRepo.delete(test);
    }


    @Transactional
    public synchronized Test updateTest(Long testId, TestDto testDto) {
        Test test = getById(testId);
        ProjectDto project = projectService.getProjectById(testDto.getProjectId());
        Suite suite = null;
        if (testDto.getSuiteId() != null) {
            suite = suiteService.getSuiteById(testDto.getSuiteId());
        }
        test.setTitle(testDto.getTitle());
        test.setProjectId(projectMapper.toEntity(project));
        test.setSuiteId(suite);
        test.setDescription(testDto.getDescription());
        test.setStatus(testDto.getStatus());
        //test.setSteps(testDto.getSteps());
        test.setAutomated(testDto.getAutomated());
        testRepo.save(test);
        return test;
    }


}