package ru.tms.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.tms.api.CommentController;
import ru.tms.dto.Comment;
import ru.tms.entity.CommentEntity;
import ru.tms.mappers.CommentMapper;
import ru.tms.producer.CommentCommentKafkaProducerImpl;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @InjectMocks
    private CommentController controller;

    @Mock
    private CommentServiceImpl commentService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentCommentKafkaProducerImpl kafkaProducer;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Метод GET /comment/{commentId} должен корректно вернуть dto")
    void testGetComment() throws Exception {
        // Arrange
        Long commentId = 1L;
        Comment expectedComment = new Comment(commentId, "New comment", Instant.now(), "John Doe",
                "project", 1L, false);
        CommentEntity expectedCommentEntity = new CommentEntity(commentId, "New comment", Instant.now(),
                "John Doe", "project", 1L, false);

        when(commentService.getCommentById(eq(commentId))).thenReturn(expectedCommentEntity);
        when(commentMapper.toDto(any(CommentEntity.class))).thenReturn(expectedComment);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/tms_comment/comment/" + commentId));

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedComment.id()))
                .andExpect(jsonPath("$.content").value(expectedComment.content()))
                .andExpect(jsonPath("$.author").value(expectedComment.author()))
                .andExpect(jsonPath("$.targetType").value(expectedComment.targetType()))
                .andExpect(jsonPath("$.targetObjectId").value(expectedComment.targetObjectId()))
                .andExpect(jsonPath("$.changed").value(expectedComment.changed()));
        verify(commentService, times(1)).getCommentById(commentId);
        verify(commentMapper, times(1)).toDto(expectedCommentEntity);
    }

    @Test
    @DisplayName("Метод GET /comments/{targetType}/{targetObjectId} должен корректно вернуть список dto")
    void testGetComments() throws Exception {
        // Arrange
        Long commentId = 1L;
        String targetType = "project";
        long targetObjectId = 1L;
        Comment expectedComment = new Comment(commentId, "New comment", null, "John Doe",
                targetType, targetObjectId, false);
        List<Comment> comments = List.of(expectedComment);

        when(commentService.getAllComments(eq(targetType), eq(targetObjectId))).thenReturn(comments);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/tms_comment/comments/" + targetType + "/"
                + targetObjectId));

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(expectedComment.id()))
                .andExpect(jsonPath("$.[0].content").value(expectedComment.content()))
                .andExpect(jsonPath("$.[0].author").value(expectedComment.author()))
                .andExpect(jsonPath("$.[0].targetType").value(expectedComment.targetType()))
                .andExpect(jsonPath("$.[0].targetObjectId").value(expectedComment.targetObjectId()))
                .andExpect(jsonPath("$.[0].changed").value(expectedComment.changed()));
        verify(commentService, times(1)).getAllComments(eq(targetType), eq(targetObjectId));
    }

    @Test
    @DisplayName("Метод POST /comment должен корректно создать новый dto")
    void testCreateComment() throws Exception {
        //Arrange
        Long commentId = 1L;
        String targetType = "project";
        long targetObjectId = 1L;
        Comment requestComment = new Comment(commentId, "New comment", null, "John Doe",
                targetType, targetObjectId, false);

        when(commentService.createComment(any(Comment.class))).thenReturn(requestComment);

        //Act
        ResultActions resultActions = mockMvc.perform(post("/tms_comment/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestComment)));

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestComment.id()))
                .andExpect(jsonPath("$.content").value(requestComment.content()))
                .andExpect(jsonPath("$.author").value(requestComment.author()))
                .andExpect(jsonPath("$.targetType").value(requestComment.targetType()))
                .andExpect(jsonPath("$.targetObjectId").value(requestComment.targetObjectId()))
                .andExpect(jsonPath("$.changed").value(requestComment.changed()));
        verify(commentService, times(1)).createComment(eq(requestComment));
    }

    @Test
    @DisplayName("Метод PUT /comment/{commentId} успешно обновит dto")
    void testUpdateComment() throws Exception {
        //Arrange
        long commentId = 1L;
        Comment updatedComment = new Comment(commentId, "Updated comment", null,
                "John Doe", "project", 1L, false);

        when(commentService.updateComment(any(Comment.class), anyLong())).thenReturn(updatedComment);

        //Act
        ResultActions resultActions = mockMvc.perform(put("/tms_comment/comment/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedComment)));

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedComment.id()))
                .andExpect(jsonPath("$.content").value(updatedComment.content()))
                .andExpect(jsonPath("$.author").value(updatedComment.author()))
                .andExpect(jsonPath("$.targetType").value(updatedComment.targetType()))
                .andExpect(jsonPath("$.targetObjectId").value(updatedComment.targetObjectId()))
                .andExpect(jsonPath("$.changed").value(updatedComment.changed()));
        verify(commentService, times(1)).updateComment(eq(updatedComment), eq(commentId));
    }

    @Test
    @DisplayName("Метод DELETE /comment/{commentId} должен корректно удалить dto")
    void testDeleteComment() throws Exception {
        //Arrange
        Long commentId = 1L;

        //Act
        ResultActions resultActions = mockMvc.perform(delete("/tms_comment/comment/" + commentId));

        //Assert
        resultActions.andExpect(status().isOk());
        verify(commentService, times(1)).deleteComment(eq(commentId));
    }

    private String asJsonString(final Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
