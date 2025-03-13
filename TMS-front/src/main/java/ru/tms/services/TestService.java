package ru.tms.services.test;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.tms.components.Test;
import ru.tms.dto.TestDto;
import ru.tms.services.WebClientServiceBack;

import java.util.*;

@Service
public class TestService implements WebClientTest{

    private final WebClientServiceBack webClientService;

    public TestService(WebClientServiceBack webClientServiceBack) {
        this.webClientService = webClientServiceBack;
    }

    @Override
    public Test getTestById(Long testId) throws NoSuchElementException {
        return webClientService.sendRequest("/api/test/{testId}",
                WebClientServiceBack.HttpMethod.GET,
                Map.of("testId", testId),
                null,
                Test.class,
                null);
    }

    @Override
    public List<Test> getAllTestByProjectId(Long projectId) {
        return webClientService.sendRequest("/api/test/projectId/{projectId}",
                WebClientServiceBack.HttpMethod.GET,
                Map.of("projectId", projectId),
                null,
                new ParameterizedTypeReference<List<Test>>(){},
                Collections.emptyList());
    }

    @Override
    public List<Test> getAllTestBySuiteId(Long suiteId) {
        return webClientService.sendRequest("/api/test/suiteId/{suiteId}",
                WebClientServiceBack.HttpMethod.GET,
                Map.of("suiteId", suiteId),
                null,
                new ParameterizedTypeReference<List<Test>>(){},
                Collections.emptyList());
    }

    @Override
    public List<Test> getTestsInParentSuiteAndChildSuites(Long suiteId) {
        return webClientService.sendRequest("/api/test/suiteIdAndChild/{suiteId}/",
                WebClientServiceBack.HttpMethod.GET,
                Map.of("suiteId", suiteId),
                null,
                new ParameterizedTypeReference<List<Test>>(){},
                Collections.emptyList());
    }

    @Override
    public synchronized Test createTest(TestDto testDto) {
        return webClientService.sendRequest("/api/test",
                WebClientServiceBack.HttpMethod.POST,
                null,
                testDto,
                Test.class,
                null);
    }

    @Override
    public synchronized void deleteTest(Long testId) {
        TestDto test = webClientService.sendRequest("/api/test/{testId}",
                WebClientServiceBack.HttpMethod.DELETE,
                Map.of("testId", testId),
                null,
                TestDto.class,
                null);
    }

    @Override
    public synchronized Test updateTest(Long testId, TestDto testDto) {
        return webClientService.sendRequest("/api/test/{testId}",
                WebClientServiceBack.HttpMethod.PUT,
                Map.of("testId", testId),
                testDto,
                Test.class,
                null);
    }
}
