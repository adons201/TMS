package ru.tms.services;

import ru.tms.dto.CommentDto;
import ru.tms.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment getCommentById(Long commentId);

    List<CommentDto> getAllComments(String targetType, Long targetObjectId);

    CommentDto createComment(CommentDto commentDto);

    CommentDto updateComment(CommentDto commentDto, Long commentId);

    void deleteComment(Long commentId);
}
