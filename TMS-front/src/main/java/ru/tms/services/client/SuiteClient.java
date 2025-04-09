package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.SuiteDto;

import java.util.List;

public interface SuiteClient {

    SuiteDto getSuiteById(Long id);

    SuiteDto createSuite(SuiteDto suiteDto);

    SuiteDto updateSuite(Long id, SuiteDto suiteDto);

    ResponseEntity<Void> deleteSuite(Long id);

    List<SuiteDto> getAllSuitesByProject(Long projectId);

}
