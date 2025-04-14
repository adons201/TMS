package ru.tms.mappers;

import org.mapstruct.Mapper;
import ru.tms.dto.Comment;
import ru.tms.dto.ReducedComment;
import ru.tms.entity.CommentEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toDto(CommentEntity comment);
    List<Comment> toDto(List<CommentEntity> comments);
    CommentEntity toEntity(Comment comment);
    ReducedComment toReduced(Comment comment);
}
