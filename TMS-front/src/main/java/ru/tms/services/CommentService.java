package ru.tms.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import ru.tms.dto.Comment;
import ru.tms.services.client.CommentClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentService implements CommentClient {

    private final RestClient restClient;
    private final Logger logger = Logger.getLogger(CommentService.class.getName());

    public CommentService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Optional<Comment> getComment(Long commentId) {
        try {
            var response = this.restClient.get()
                    .uri("/comment/{commentId}", commentId)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            (status, clientResponse) -> new RuntimeException("Ошибка при получении комментария: "
                                    + status))
                    .body(Comment.class);
            return Optional.ofNullable(response);
        } catch (RuntimeException e) {
            logger.log(Level.FINER, "Ошибка при выполнении запроса: "+ e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> getAllComments(String targetType, Long targetObjectId) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("targetType", targetType);
        uriVariables.put("targetObjectId", targetObjectId);
        try {
            return this.restClient.get()
                    .uri("/comments/{targetType}/{targetObjectId}", uriVariables)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            (status, clientResponse) -> new RuntimeException("Ошибка при получении комментариев: "
                                    + status))
                    .body(new ParameterizedTypeReference<List<Comment>>(){});
        } catch (RuntimeException e) {
            logger.log(Level.FINER, "Ошибка при выполнении запроса: "+ e.getMessage());
            return List.of();
        }
    }

    @Override
    public Optional<Comment> createComment(Comment comment) {
        try {
            return Optional.ofNullable(this.restClient.post()
                    .uri("/comment")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(comment)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            (status, clientResponse) -> new RuntimeException("Ошибка при создании комментариев: "
                                    + status))
                    .body(Comment.class));
        } catch (RuntimeException e) {
            logger.log(Level.FINER, "Ошибка при выполнении запроса: "+ e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Comment> updateComment(Long commentId, Comment comment) {
        try {
            return Optional.ofNullable(this.restClient.put()
                    .uri("/comment/{commentId}", commentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(comment)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            (status, clientResponse) -> new RuntimeException("Ошибка при обновлении комментариев: "
                                    + status))
                    .body(Comment.class));
        } catch (RuntimeException e) {
            logger.log(Level.FINER, "Ошибка при выполнении запроса: "+ e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) {
        try {
            return this.restClient.delete()
                    .uri("/comment/{commentId}", commentId)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            (status, clientResponse) -> new RuntimeException("Ошибка при удалении комментария: "
                                    + status))
                    .toBodilessEntity();
        } catch (RuntimeException e) {
            logger.log(Level.FINER, "Ошибка при выполнении запроса: " + e.getMessage());
            return null;
        }
    }
}
