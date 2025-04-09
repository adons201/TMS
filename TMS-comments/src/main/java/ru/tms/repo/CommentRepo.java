package ru.tms.repo;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.tms.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(String targetType, Long targetId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Comment> findById(Long id);
}
