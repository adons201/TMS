package ru.tms.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import ru.tms.dto.Comment;
import ru.tms.entity.CommentEntity;
import ru.tms.exceptions.InvalidCommentDataException;
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
    public CommentEntity getCommentById(Long commentId) {
        return commentRepo.findById(commentId).orElseThrow();
    }

    @Override
    public List<Comment> getAllComments(String targetType, Long targetObjectId) {
        return commentMapper.toDto(commentRepo
                .getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(targetType, targetObjectId));
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        if (comment == null || comment.content() == null || comment.content().isEmpty()) {
            throw new InvalidCommentDataException("Comment content cannot be empty");
        }
        CommentEntity commentEntity = commentMapper.toEntity(comment);
        commentEntity.setChanged(false);
        return commentMapper.toDto(commentRepo.save(commentEntity));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Comment updateComment(Comment comment, Long commentId) {
        if (comment == null || comment.content() == null || comment.content().isEmpty()) {
            throw new InvalidCommentDataException("Comment content cannot be empty");
        }
        CommentEntity commentEntity = this.getCommentById(commentId);
        commentEntity.setContent(comment.content());
        commentEntity.setChanged(true);
        return commentMapper.toDto(commentRepo.save(commentEntity));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteComment(Long commentId) {
        CommentEntity commentEntity = this.getCommentById(commentId);
        commentRepo.deleteById(commentEntity.getId());
    }

}
