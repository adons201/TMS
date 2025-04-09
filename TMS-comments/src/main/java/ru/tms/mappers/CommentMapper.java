package ru.tms.mappers;

import org.mapstruct.Mapper;
import ru.tms.dto.CommentDto;
import ru.tms.entity.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    List<CommentDto> toDto(List<Comment> comments);
    Comment toEntity(CommentDto commentDto);
}
