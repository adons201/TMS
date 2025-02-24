package ru.tms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tms.entity.Test;
import ru.tms.dto.TestDto;
import ru.tms.services.TestService;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@Tag(name = "Test", description = "the Test in TMS API")
public class TestController {

    private TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "Get all Test in all Suite", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Long testId) {
        try {
            Test test = testService.getById(testId);
            return new ResponseEntity<>(test, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all Test in all Suite", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/projectId/{projectId}")
    public Collection<Test> getAllTestByProjectId(@PathVariable Long projectId) {
        return testService.getAllByProjectId(projectId);
    }

    @Operation(summary = "Get all Test in Suite", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/suiteId/{suiteId}")
    public Collection<Test> getAllTestBySuiteId(@PathVariable Long suiteId) {
        return testService.getAllBySuiteId(suiteId);
    }

    @Operation(summary = "Get all Test in Suite and Child Suites", description = "Return all Test in all Projects", tags = {"Test"})
    @GetMapping(value = "/test/suiteIdAndChild/{suiteId}/")
    public Collection<Test> getTestsInParentSuiteAndChildSuites(@PathVariable Long suiteId) {
        return testService.getAllAndChildBySuiteId(suiteId);
    }

    @Operation(summary = "Add Test", description = "Return created Test", tags = {"Test"})
    @PostMapping(value = "/test", consumes = "application/json")
    public Test createTest(@Validated @RequestBody TestDto testDto) {
        Test result = testService.insert(testDto);
        return result;
    }

    @Operation(summary = "Update Test", description = "Return updated Test", tags = {"Suite"})
    @PutMapping(value = "/test/{testId}", consumes = "application/json")
    public Test updateTest(@PathVariable Long testId, @Validated @RequestBody TestDto testDto) {
        Test result = testService.updateTest(testId, testDto);
        return result;
    }

    @Operation(summary = "Delete Test", description = "", tags = {"Test"})
    @DeleteMapping(value = "/test/{testId}")
    public void deleteTest(@PathVariable Long testId) {
        testService.del(testId);
    }

}