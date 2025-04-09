package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tms.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(String targetType, Long targetId);
}
