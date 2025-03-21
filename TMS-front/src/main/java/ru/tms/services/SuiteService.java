package ru.tms.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import ru.tms.components.Suite;
import ru.tms.components.Test;
import ru.tms.dto.SuiteChild;
import ru.tms.dto.SuiteDto;
import ru.tms.models.ParentWebModel;
import ru.tms.models.SuiteWebModel;
import ru.tms.models.TestWebModel;
import ru.tms.services.client.SuiteClient;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SuiteService implements SuiteClient {

    private final TestService testService;
    private final RestClient restClient;

    @Override
    public Suite getSuite(Long suiteId) {
        return this.restClient.get()
                .uri("/api/suite{suiteId}",suiteId)
                .retrieve()
                .body(Suite.class);
    }

    @Override
    public List<Suite> getAllSuitesByProject(Long projectId) {
        return this.restClient.get()
                .uri("/api/suites/{projectId}",projectId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Suite>>(){});
    }

    @Override
    public SuiteDto createSuite(SuiteDto suiteDto) {
        return this.restClient.post()
                .uri("/api/suite")
                .contentType(MediaType.APPLICATION_JSON)
                .body(suiteDto)
                .retrieve()
                .body(SuiteDto.class);
    }

    @Override
    public SuiteDto updateSuite(Long suiteId, SuiteDto suiteDto) {
        return this.restClient.post()
                .uri("/api/suite/{suiteId}", suiteId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(suiteDto)
                .retrieve()
                .body(SuiteDto.class);
    }

    @Override
    public ResponseEntity<Void> deleteSuite(Long suiteId) {
        return this.restClient.delete()
                .uri("/api/suite/{suiteId}", suiteId)
                .retrieve()
                .toBodilessEntity();
    }

    public List<SuiteChild> getSuiteHierarchy(Long projectId) {
        return suiteChild(projectId);
    }

    public List<ParentWebModel> getSuiteHierarchyNew(Long projectId) {
        return suiteChildNew(projectId);
    }

    public List<SuiteChild> suiteChild(Long projectId) {
        List<SuiteChild> suiteChildren = new LinkedList<>();
        Collection<Suite> allSuite = getAllSuitesByProject(projectId);
        List<Suite> parent = allSuite.stream().filter(x -> x.getParentId() == null).collect(Collectors.toList());
        List<Suite> childs = allSuite.stream().filter(x -> x.getParentId() != null).collect(Collectors.toList());
        parent.forEach(x -> {
            suiteChildren.add(new SuiteChild(
                    x,
                    children(x, childs).stream().collect(Collectors.toList()),
                    allChildren(childs, x).stream().collect(Collectors.toList()),
                    testService.getAllTestBySuiteId(x.getId())));
        });
        return suiteChildren;
    }

    public List<ParentWebModel> suiteChildNew(Long projectId) {
        List<ParentWebModel> suiteChildren = new LinkedList<>();
        List<Suite> allSuites = getAllSuitesByProject(projectId);
        List<Test> allTests = testService.getAllTestByProjectId(projectId);
        List<Suite> parent = allSuites.stream().filter(x -> x.getParentId() == null).collect(Collectors.toList());
        List<Suite> children = allSuites.stream().filter(x -> x.getParentId() != null).collect(Collectors.toList());
        parent.forEach(x -> {
            suiteChildren.add(new SuiteWebModel(
                    x,
                    new ArrayList<>(childrenNew(x, children, allTests))));
        });
        return suiteChildren;
    }

    private Collection<SuiteChild> children(Suite parent, List<Suite> childs) {
        Collection<SuiteChild> suiteChildren = new LinkedList<>();
        childs.stream()
                .filter(y -> y.getParentId() == parent)
                .collect(Collectors.toList())
                .forEach(x -> {
                    suiteChildren.add(
                            new SuiteChild(
                                    x,
                                    new ArrayList<>(children(x, childs)),
                                    new ArrayList<>(allChildren(childs, x)),
                                    testService.getAllTestBySuiteId(x.getId())));
                });
        return suiteChildren;
    }

    private Collection<ParentWebModel> childrenNew(Suite parent, List<Suite> allSuite, List<Test> allTests) {
        Collection<ParentWebModel> suiteChildren = new LinkedList<>();
        allTests.stream()
                .filter(y -> y.getSuiteId().getId().equals(parent.getId()))
                .collect(Collectors.toList())
                .forEach(x -> {
                    suiteChildren.add(
                            new TestWebModel(x));
                });
        allSuite.stream()
                .filter(y -> y.getParentId().getId().equals(parent.getId()))
                .collect(Collectors.toList())
                .forEach(x -> {
                    suiteChildren.add(
                            new SuiteWebModel(x, new ArrayList<>(childrenNew(x, allSuite, allTests))));
                });
        return suiteChildren;
    }

    private Collection<Suite> allChildren(Collection<Suite> suites, Suite parentSuite) {
        Collection<Suite> result = new LinkedList<>();
        Collection<Suite> child = suites.stream().filter(x -> x.getParentId() == parentSuite).collect(Collectors.toList());
        child.forEach(x -> {
            result.add(x);
            allChildren(suites, x).forEach(y -> {
                result.add(y);
            });
        });
        return result;
    }

}
