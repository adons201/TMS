package ru.tms.services;

import org.springframework.stereotype.Service;
import ru.tms.dto.CommentDto;
import ru.tms.entity.Comment;
import ru.tms.mappers.CommentMapper;
import ru.tms.repo.CommentRepo;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepo commentRepo, CommentMapper commentMapper) {
        this.commentRepo = commentRepo;
        this.commentMapper = commentMapper;
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepo.findById(commentId).get();
    }

    @Override
    public List<CommentDto> getAllComments(String targetType, Long targetObjectId) {
        return commentMapper.toDto(commentRepo
                .getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(targetType, targetObjectId));
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setChanged(false);
        return commentMapper.toDto(commentRepo.save(comment));
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto, Long commentId) {
        Comment comment = this.getCommentById(commentId);
        comment.setContent(commentDto.content());
        comment.setChanged(true);
        return commentMapper.toDto(commentRepo.save(comment));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepo.deleteById(commentId);
    }

}
