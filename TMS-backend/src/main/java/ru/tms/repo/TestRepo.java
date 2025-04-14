package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tms.entity.TestEntity;

import java.util.List;

@Repository
public interface TestRepo extends JpaRepository<TestEntity, Long> {
    @Query("select test from TestEntity test where test.projectId.id = :id")
    List<TestEntity> findAllTestsByProject(Long id);

    @Query("select test from TestEntity test where test.suiteId.id = :id")
    List<TestEntity> findAllTestsBySuite(Long id);

    @Query("select test from TestEntity test where test.suiteId.id in(:ids)")
    List<TestEntity> findAllTestsBySuites(List<Long> ids);
}
