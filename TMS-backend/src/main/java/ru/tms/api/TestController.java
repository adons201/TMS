package ru.tms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tms.dto.Test;
import ru.tms.mappers.TestMapper;
import ru.tms.services.TestServiceImpl;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@Tag(name = "Test", description = "the Test in TMS API")
public class TestController {

    private final TestServiceImpl testServiceImpl;
    private final TestMapper testMapper;

    public TestController(TestServiceImpl testServiceImpl, TestMapper testMapper) {
        this.testServiceImpl = testServiceImpl;
        this.testMapper = testMapper;
    }

    @Operation(summary = "Get all Test in all Suite", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Long testId) {
        try {
            Test test = testMapper.toDto(testServiceImpl.getTestById(testId));
            return new ResponseEntity<>(test, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all Test in all Suite", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/projectId/{projectId}")
    public Collection<Test> getAllTestByProjectId(@PathVariable Long projectId) {
        return testServiceImpl.getAllTestsByProjectId(projectId);
    }

    @Operation(summary = "Get all Test in Suite", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/suiteId/{suiteId}")
    public Collection<Test> getAllTestBySuiteId(@PathVariable Long suiteId) {
        return testServiceImpl.getAllTestsBySuiteId(suiteId);
    }

    @Operation(summary = "Get all Test in Suite and Child Suites", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/suiteIdAndChild/{suiteId}/")
    public Collection<Test> getTestsInParentSuiteAndChildSuites(@PathVariable Long suiteId) {
        return testServiceImpl.getAllAndChildBySuiteId(suiteId);
    }

    @Operation(summary = "Add Test", description = "Return created Test", tags = {"Test"})
    @PostMapping(value = "/test", consumes = "application/json")
    public Test createTest(@Validated @RequestBody Test test) {
        return testServiceImpl.createTest(test);
    }

    @Operation(summary = "Update Test", description = "Return updated Test", tags = {"Suite"})
    @PutMapping(value = "/test/{testId}", consumes = "application/json")
    public Test updateTest(@PathVariable Long testId, @Validated @RequestBody Test test) {
        return testServiceImpl.updateTest(testId, test);
    }

    @Operation(summary = "Delete Test", description = "", tags = {"Test"})
    @DeleteMapping(value = "/test/{testId}")
    public void deleteTest(@PathVariable Long testId) {
        testServiceImpl.deleteTest(testId);
    }
}