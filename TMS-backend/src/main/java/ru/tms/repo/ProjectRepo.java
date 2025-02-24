package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tms.entity.Project;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
}
