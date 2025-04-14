package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tms.entity.ProjectEntity;

@Repository
public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {
}
