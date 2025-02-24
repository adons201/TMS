package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tms.entity.Test;

import java.util.List;

@Repository
public interface TestRepo extends JpaRepository<Test, Long> {
    @Query("select test from Test test where test.projectId.id = :id")
    List<Test> findAllTestsByProject(Long id);

    @Query("select test from Test test where test.suiteId.id = :id")
    List<Test> findAllTestsBySuite(Long id);

    @Query("select test from Test test where test.suiteId.id in(:ids)")
    List<Test> findAllTestsBySuites(List<Long> ids);
}
