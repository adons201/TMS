package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tms.components.Test;
import ru.tms.dto.TestDto;

import java.util.List;

public interface TestClient {

    Test getTest(Long testId);

    Test createTest(TestDto testDto);

    Test updateTest(Long id, TestDto testDto);

    ResponseEntity<Void> deleteTest(Long testId);

    List<Test> getAllTestByProjectId(Long projectId);

    List<Test> getAllTestBySuiteId(Long suiteId);

}
