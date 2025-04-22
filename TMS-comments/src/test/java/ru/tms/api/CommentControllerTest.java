package ru.tms.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.tms.dto.Comment;
import ru.tms.entity.CommentEntity;
import ru.tms.exceptions.InvalidCommentDataException;
import ru.tms.mappers.CommentMapper;
import ru.tms.producer.CommentCommentKafkaProducerImpl;
import ru.tms.services.CommentServiceImpl;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommentController.class)
@ActiveProfiles("cloudconfig")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentServiceImpl commentService;

    @MockitoBean
    private CommentMapper commentMapper;

    @MockitoBean
    private CommentCommentKafkaProducerImpl kafkaProducer;

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

    @Test
    @DisplayName("Метод GET /comment/{commentId} должен вернуться статус 404")
    void getComment_CommentNoSuch_ShouldReturnNotFound() throws Exception {
        //Arrange
        Long commentId = 1L;

        when(commentService.getCommentById(eq(commentId))).thenThrow(new NoSuchElementException());

        //Act & Assert
        mockMvc.perform(get("/tms_comment/comment/" + commentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("No value present")))
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("Метод POST /comment должен вернуться статус 400")
    void createComment_InvalidCommentData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Long commentId = 1L;
        Comment commentToUpdate = new Comment(commentId, "", null, "John Doe",
                "project", 1L, false);

        when(commentService.createComment(commentToUpdate))
                .thenThrow(new InvalidCommentDataException("Comment content cannot be null or empty"));


        //Act & Assert
        mockMvc.perform(post("/tms_comment/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentToUpdate))).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Comment content cannot be null or empty")))
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @DisplayName("Метод PUT /comment/{commentId} должен вернуться статус 400")
    void updateComment_InvalidCommentData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        Long commentId = 1L;
        Comment commentToUpdate = new Comment(commentId, "", null, "John Doe",
                "project", 1L, false);

        when(commentService.updateComment(commentToUpdate, commentId))
                .thenThrow(new InvalidCommentDataException("Comment content cannot be null or empty"));


        //Act & Assert
        mockMvc.perform(put("/tms_comment/comment/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(commentToUpdate))).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Comment content cannot be null or empty")))
                .andExpect(jsonPath("$.status", is(400)));
    }

    private String asJsonString(final Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
