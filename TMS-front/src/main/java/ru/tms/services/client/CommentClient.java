package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.Comment;

import java.util.List;

public interface CommentClient {

    Comment getComment(Long commentId);

    List<Comment> getAllComments(String targetType, Long targetObjectId);

    Comment createComment(Comment comment);

    Comment updateComment(Long commentId, Comment comment);

    ResponseEntity<Void> deleteComment(Long commentId);

}
