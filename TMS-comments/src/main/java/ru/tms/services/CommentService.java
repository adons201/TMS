package ru.tms.services;

import ru.tms.dto.Comment;
import ru.tms.entity.CommentEntity;

import java.util.List;

public interface CommentService {

    CommentEntity getCommentById(Long commentId);

    List<Comment> getAllComments(String targetType, Long targetObjectId);

    Comment createComment(Comment comment);

    Comment updateComment(Comment comment, Long commentId);

    void deleteComment(Long commentId);
}
