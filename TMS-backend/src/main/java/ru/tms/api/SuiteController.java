package ru.tms.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tms.dto.SuiteDto;
import ru.tms.entity.Suite;
import ru.tms.mappers.SuiteMapper;
import ru.tms.services.SuiteServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@Tag(name = "Suite", description = "the Suite in TMS API")
public class SuiteController {

    SuiteServiceImpl suiteServiceImpl;
    SuiteMapper suiteMapper;

    public SuiteController(SuiteServiceImpl suiteServiceImpl, SuiteMapper suiteMapper) {
        this.suiteServiceImpl = suiteServiceImpl;
        this.suiteMapper = suiteMapper;
    }

    @Operation(summary = "Get Suite", description = "Return Suite", tags = {"Suite"})
    @GetMapping(value = "/suite/{suiteId}")
    public ResponseEntity<SuiteDto> getSuiteById(@PathVariable Long suiteId) {
        try {
            SuiteDto suiteDto = suiteMapper.toDto(this.suiteServiceImpl.getSuiteById(suiteId));
            return new ResponseEntity<>(suiteDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all Suites in Project", description = "Return all Suites in Project", tags = {"Suite"})
    @GetMapping(value = "/suites/{projectId}")
    public List<SuiteDto> getAllSuitesByProject(@PathVariable Long projectId) {
        return suiteServiceImpl.getAllSuitesByProject(projectId);
    }

    @Operation(summary = "Get all Child Suites in Suite", description = "Return all Child Suites in Suite", tags = {"Suite"})
    @GetMapping(value = "/childAllSuites/{suiteId}")
    public Collection<SuiteDto> getAllChildSuiteBySuite(@PathVariable Long suiteId) {
        return suiteServiceImpl.getAllChildSuitesBySuite(suiteId);
    }

    @Operation(summary = "Get Child Suites in Suite", description = "Return Child Suites in Suite", tags = {"Suite"})
    @GetMapping(value = "/childSuites/{suiteId}")
    public Collection<SuiteDto> getChildSuiteBySuite(@PathVariable Long suiteId) {
        return suiteServiceImpl.getChildSuitesBySuite(suiteId);
    }

    @Operation(summary = "Add Suite", description = "Return created Suite", tags = {"Suite"})
    @PostMapping(value = "/suite", consumes = "application/json")
    public SuiteDto createSuite(@Validated @RequestBody SuiteDto suiteDto) {
        return suiteServiceImpl.createSuite(suiteDto);
    }

    @Operation(summary = "Update Suite", description = "Return updated Suite", tags = {"Suite"})
    @PutMapping(value = "/suite/{suiteId}", consumes = "application/json")
    public SuiteDto updateSuite(@PathVariable Long suiteId, @Validated @RequestBody SuiteDto suiteDto) {
        return suiteServiceImpl.updateSuite(suiteId, suiteDto);
    }

    @Operation(summary = "Delete Suite", description = "", tags = {"Suite"})
    @DeleteMapping(value = "/suite/{suiteId}")
    public void deleteSuite(@PathVariable Long suiteId) {
        suiteServiceImpl.deleteSuite(suiteId);
    }
}
