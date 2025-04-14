package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.Test;

import java.util.List;

public interface TestClient {

    Test getTest(Long testId);

    Test createTest(Test test);

    Test updateTest(Long id, Test test);

    ResponseEntity<Void> deleteTest(Long testId);

    List<Test> getAllTestByProjectId(Long projectId);

    List<Test> getAllTestBySuiteId(Long suiteId);

}
