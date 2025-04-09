package ru.tms.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tms.dto.CommentDto;
import ru.tms.entity.Comment;
import ru.tms.mappers.CommentMapper;
import ru.tms.repo.CommentRepo;

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
        Long commentId =1L;
        Comment expectedComment = new Comment();
        when(commentRepo.findById(commentId)).thenReturn(Optional.of(expectedComment));

        // Act
        Comment actualComment = commentService.getCommentById(commentId);

        // Assert
        assertThat(actualComment).isEqualTo(expectedComment);
        verify(commentRepo).findById(commentId);
    }

    @Test
    @DisplayName("Должен корректно вернуться List объектов комментария по заданным targetType, targetObjectId")
    void shouldReturnAllComments() {
        lenient();
        // Arrange
        String targetType = "project";
        Long targetObjectId = 1L;
        List<Comment> comments = new ArrayList<>();
        List<CommentDto> expectedDto = new ArrayList<>();
        when(commentRepo.getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(eq(targetType), eq(targetObjectId)))
                .thenReturn(comments);
        when(commentMapper.toDto(anyList()))
                .thenAnswer(invocation -> {
                    List<Comment> inputComments = invocation.getArgument(0);
                    List<CommentDto> result = new ArrayList<>();

                    for (Comment comment : inputComments) {
                        result.add(new CommentDto(comment.getId(), comment.getContent(), comment.getCreatedAt(),
                                comment.getAuthor(), comment.getTargetType(), comment.getTargetObjectId(), comment.getChanged()));
                    }
                    return result;
                });

        // Act
        List<CommentDto> actualDto = commentService.getAllComments(targetType, targetObjectId);

        // Assert
        assertThat(actualDto).isEqualTo(expectedDto);
        verify(commentRepo).getByTargetTypeAndTargetObjectIdOrderByCreatedAtDesc(eq(targetType), eq(targetObjectId));
        verify(commentMapper, times(comments.size())).toDto(any(Comment.class));
    }

    @Test
    @DisplayName("Должен корректно создать коммент")
    void shouldCreateComment() {
        // Arrange
        CommentDto commentDto = new CommentDto(1L, "New comment", LocalDateTime.now(),"John Doe", "project", 1L,  false);
        Comment expectedComment = new Comment();
        expectedComment.setContent(commentDto.content());
        expectedComment.setAuthor(commentDto.author());
        expectedComment.setTargetType(commentDto.targetType());
        expectedComment.setTargetObjectId(commentDto.targetObjectId());
        expectedComment.setChanged(false);
        when(commentMapper.toEntity(commentDto)).thenReturn(expectedComment);
        when(commentRepo.save(expectedComment)).thenReturn(expectedComment);
        when(commentMapper.toDto(expectedComment)).thenReturn(commentDto);

        // Act
        CommentDto actualDto = commentService.createComment(commentDto);

        // Assert
        assertThat(actualDto).isEqualTo(commentDto);
        verify(commentMapper).toEntity(commentDto);
        verify(commentRepo).save(expectedComment);
        verify(commentMapper).toDto(expectedComment);
    }

    @Test
    @DisplayName("Должен корректно обновить коммент")
    void shouldUpdateComment() {
        // Arrange
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto(1L, "Updated comment", LocalDateTime.now(),"John Doe", "project", 1L,  false);
        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setContent("Old comment");
        existingComment.setAuthor("John Doe");
        existingComment.setTargetType("project");
        existingComment.setTargetObjectId(1L);
        existingComment.setChanged(false);
        when(commentRepo.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepo.save(existingComment)).thenReturn(existingComment);
        when(commentMapper.toDto(existingComment)).thenReturn(commentDto);

        // Act
        CommentDto actualDto = commentService.updateComment(commentDto, commentId);

        // Assert
        assertThat(actualDto).isEqualTo(commentDto);
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