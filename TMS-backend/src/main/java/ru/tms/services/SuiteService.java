package ru.tms.services;

import ru.tms.dto.SuiteDto;
import ru.tms.entity.Suite;

import java.util.List;

public interface SuiteService {

    List<SuiteDto> getAllSuitesByProject(Long projectId);

    List<SuiteDto> getAllChildSuitesBySuite(Long suiteId);

    List<SuiteDto> getChildSuitesBySuite(Long suiteId);

    Suite getSuiteById(Long id);

    SuiteDto createSuite(SuiteDto suiteDto);

    SuiteDto updateSuite(Long suiteId, SuiteDto suiteDto);

    void deleteSuite(Long id);
}
