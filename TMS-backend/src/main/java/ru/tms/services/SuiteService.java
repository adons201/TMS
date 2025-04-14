package ru.tms.services;

import ru.tms.dto.Suite;
import ru.tms.entity.SuiteEntity;

import java.util.List;

public interface SuiteService {

    List<Suite> getAllSuitesByProject(Long projectId);

    List<Suite> getAllChildSuitesBySuite(Long suiteId);

    List<Suite> getChildSuitesBySuite(Long suiteId);

    SuiteEntity getSuiteById(Long id);

    Suite createSuite(Suite suite);

    Suite updateSuite(Long suiteId, Suite suite);

    void deleteSuite(Long id);
}
