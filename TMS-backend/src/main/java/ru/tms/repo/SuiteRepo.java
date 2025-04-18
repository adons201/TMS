package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tms.entity.SuiteEntity;

import java.util.List;

@Repository
public interface SuiteRepo extends JpaRepository<SuiteEntity, Long> {

    @Query("select suite from SuiteEntity suite where suite.project.id = :id")
    List<SuiteEntity> findAllSuiteByProject(Long id);

    @Query(value = "WITH RECURSIVE nodes AS (\n" +
            "    SELECT s1.id, s1.name, s1.description, s1.project_id, s1.parent_id\n" +
            "    FROM Suite s1 WHERE parent_id = :id\n" +
            "        UNION\n" +
            "    SELECT s2.id, s2.name, s2.description, s2.project_id, s2.parent_id\n" +
            "    FROM Suite s2, nodes s1 WHERE s2.parent_id = s1.id\n" +
            ")\n" +
            "SELECT * FROM nodes",nativeQuery = true)
    List<SuiteEntity> findAllChildSuitesBySuite(Long id);

    @Query("select suite from SuiteEntity suite where suite.parentId.id = :id")
    List<SuiteEntity> findChildSuitesBySuite(Long id);
}
