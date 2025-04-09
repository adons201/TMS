package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.CommentDto;

import java.util.List;

public interface CommentClient {

    CommentDto getComment(Long commentId);

    List<CommentDto> getAllComments(String targetType, Long targetObjectId);

    CommentDto createComment(CommentDto commentDto);

    CommentDto updateComment(Long commentId, CommentDto commentDto);

    ResponseEntity<Void> deleteComment(Long commentId);

}
