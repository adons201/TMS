package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tms.components.Suite;
import ru.tms.dto.SuiteDto;

import java.util.List;

public interface SuiteClient {

    Suite getSuite(Long id);

    SuiteDto createSuite(SuiteDto suiteDto);

    SuiteDto updateSuite(Long id, SuiteDto suiteDto);

    ResponseEntity<Void> deleteSuite(Long id);

    List<Suite> getAllSuitesByProject(Long projectId);

}
