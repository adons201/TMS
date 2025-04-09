package ru.tms.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import ru.tms.dto.TestDto;
import ru.tms.services.client.TestClient;

import java.util.*;

@RequiredArgsConstructor
public class TestService implements TestClient {

    private final RestClient restClient;

    @Override
    public List<TestDto> getAllTestByProjectId(Long projectId) {
        return this.restClient.get()
                .uri("/api/test/projectId/{projectId}", projectId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TestDto>>(){});
    }

    public List<TestDto> getAllTestBySuiteId(Long suiteId) {
        return this.restClient.get()
                .uri("/api/test/suiteId/{suiteId}", suiteId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TestDto>>(){});
    }

    @Override
    public TestDto getTest(Long testId) {
        return this.restClient.get()
                .uri("/api/test/{testId}", testId)
                .retrieve()
                .body(TestDto.class);
    }

    @Override
    public TestDto createTest(TestDto testDto) {
        return this.restClient.post()
                .uri("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .body(testDto)
                .retrieve()
                .body(TestDto.class);
    }

    @Override
    public TestDto updateTest(Long testId, TestDto testDto) {
        return this.restClient.put()
                .uri("/api/test/{testId}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(testDto)
                .retrieve()
                .body(TestDto.class);
    }

    @Override
    public ResponseEntity<Void> deleteTest(Long testId) {
        return this.restClient.delete()
                .uri("/api/test/{testId}", testId)
                .retrieve()
                .toBodilessEntity();
    }
}
