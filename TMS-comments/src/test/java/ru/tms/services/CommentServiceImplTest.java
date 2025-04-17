package ru.tms.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tms.dto.Comment;
import ru.tms.entity.CommentEntity;
import ru.tms.mappers.CommentMapper;
import ru.tms.repo.CommentRepo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepo commentRepo;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Должен корректно вернуться объект комментария по заданному commentId")
    void shouldReturnCommentWhenFoundById() {
        // Arrange
        Long commentId = 1L;
        CommentEntity expectedComment = new CommentEntity();
        when(commentRepo.findById(commentId)).thenReturn(Optional.of(expectedComment));

        // Act
        CommentEntity actualComment = commentService.getCommentById(commentId);

        // Assert
        assertThat(actualComment).isEqualTo(expectedComment);
        verify(commentRepo).findById(commentId);
    }

    @Test
    @DisplayName("Должен корректно вернуться List объектов комментария по заданным targetType, targetObjectId")
    void shouldReturnAllComments() {
        // Arrange
        String targetType = "project";
        Long targetObjectId = 1L;
        List<CommentEntity> comments = new ArrayList<>();
        List<Comment> expectedDto = new ArrayList<>();
        when(commentRepo.getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(eq(targetType), eq(targetObjectId)))
                .thenReturn(comments);
        when(commentMapper.toDto(anyList()))
                .thenAnswer(invocation -> {
                    List<CommentEntity> inputComments = invocation.getArgument(0);
                    List<Comment> result = new ArrayList<>();

                    for (CommentEntity comment : inputComments) {
                        result.add(new Comment(comment.getId(),
                                comment.getContent(),
                                comment.getCreatedAt(),
                                comment.getAuthor(),
                                comment.getTargetType(),
                                comment.getTargetObjectId(),
                                comment.getChanged()));
                    }
                    return result;
                });

        // Act
        List<Comment> actualDto = commentService.getAllComments(targetType, targetObjectId);

        // Assert
        assertThat(actualDto).isEqualTo(expectedDto);
        verify(commentRepo).getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(eq(targetType), eq(targetObjectId));
        verify(commentMapper, times(comments.size())).toDto(any(CommentEntity.class));
    }

    @Test
    @DisplayName("Должен корректно создать коммент")
    void shouldCreateComment() {
        // Arrange
        Comment comment = new Comment(1L, "New comment", Instant.now(), "John Doe",
                "project", 1L, false);
        CommentEntity expectedComment = CommentEntity.builder()
                .content(comment.content())
                .author(comment.author())
                .createdAt(comment.createdAt())
                .targetType(comment.targetType())
                .targetObjectId(comment.targetObjectId())
                .changed(comment.changed())
                .build();
        when(commentMapper.toEntity(comment)).thenReturn(expectedComment);
        when(commentRepo.save(expectedComment)).thenReturn(expectedComment);
        when(commentMapper.toDto(expectedComment)).thenReturn(comment);

        // Act
        Comment actualDto = commentService.createComment(comment);

        // Assert
        assertThat(actualDto).isEqualTo(comment);
        verify(commentMapper).toEntity(comment);
        verify(commentRepo).save(expectedComment);
        verify(commentMapper).toDto(expectedComment);
    }

    @Test
    @DisplayName("Должен корректно обновить коммент")
    void shouldUpdateComment() {
        // Arrange
        Long commentId = 1L;
        Comment comment = new Comment(1L, "Updated comment", Instant.now(), "John Doe",
                "project", 1L, false);
        CommentEntity existingComment = CommentEntity.builder()
                .id(commentId)
                .content("Old comment")
                .author("John Doe")
                .createdAt(comment.createdAt())
                .targetType("project")
                .targetObjectId(1L)
                .changed(false)
                .build();
        when(commentRepo.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepo.save(existingComment)).thenReturn(existingComment);
        when(commentMapper.toDto(existingComment)).thenReturn(comment);

        // Act
        Comment actualDto = commentService.updateComment(comment, commentId);

        // Assert
        assertThat(actualDto).isEqualTo(comment);
        verify(commentRepo).findById(commentId);
        verify(commentRepo).save(existingComment);
        verify(commentMapper).toDto(existingComment);
    }

    @Test
    void shouldDeleteComment() {
        // Arrange
        Long commentId = 1L;

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepo).deleteById(commentId);
    }
}