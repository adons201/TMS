package ru.tms.services;

import ru.tms.dto.Test;
import ru.tms.entity.TestEntity;

import java.util.List;

public interface TestService {

    TestEntity getTestById(Long id);

    List<Test> getAllTests();

    List<Test> getAllTestsByProjectId(Long id);

    List<Test> getAllTestsBySuiteId(Long id);

    List<Test> getAllAndChildBySuiteId(Long id);

    Test createTest(Test test);

    Test updateTest(Long testId, Test test);

    void deleteTest(Long id);
}
