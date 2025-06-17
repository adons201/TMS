package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentClient {

    Optional<Comment> getComment(Long commentId);

    List<Comment> getAllComments(String targetType, Long targetObjectId);

    Optional<Comment> createComment(Comment comment);

    Optional<Comment> updateComment(Long commentId, Comment comment);

    ResponseEntity<Void> deleteComment(Long commentId);

}
