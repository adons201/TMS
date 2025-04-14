package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.Suite;

import java.util.List;

public interface SuiteClient {

    Suite getSuiteById(Long id);

    Suite createSuite(Suite suite);

    Suite updateSuite(Long id, Suite suite);

    ResponseEntity<Void> deleteSuite(Long id);

    List<Suite> getAllSuitesByProject(Long projectId);

}
