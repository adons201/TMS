package ru.tms.services;

import ru.tms.dto.TestDto;
import ru.tms.entity.Test;

import java.util.List;

public interface TestService {

    Test getTestById(Long id);

    List<TestDto> getAllTests();

    List<TestDto> getAllTestsByProjectId(Long id);

    List<TestDto> getAllTestsBySuiteId(Long id);

    List<TestDto> getAllAndChildBySuiteId(Long id);

    TestDto createTest(TestDto testDto);

    TestDto updateTest(Long testId, TestDto testDto);

    void deleteTest(Long id);
}
