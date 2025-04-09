package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.TestDto;

import java.util.List;

public interface TestClient {

    TestDto getTest(Long testId);

    TestDto createTest(TestDto testDto);

    TestDto updateTest(Long id, TestDto testDto);

    ResponseEntity<Void> deleteTest(Long testId);

    List<TestDto> getAllTestByProjectId(Long projectId);

    List<TestDto> getAllTestBySuiteId(Long suiteId);

}
