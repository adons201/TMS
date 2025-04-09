package ru.tms.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import ru.tms.dto.CommentDto;
import ru.tms.services.client.CommentClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentService implements CommentClient {

    private final RestClient restClient;

    public CommentService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CommentDto getComment(Long commentId) {
        return this.restClient.get()
                .uri("/comment/{commentId}", commentId)
                .retrieve()
                .body(CommentDto.class);
    }

    @Override
    public List<CommentDto> getAllComments(String targetType, Long targetObjectId) {
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("targetType", targetType);
        uriVariables.put("targetObjectId", targetObjectId);
        return this.restClient.get()
                .uri("/comments/{targetType}/{targetObjectId}", uriVariables)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentDto>>(){});
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        return this.restClient.post()
                .uri("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(commentDto)
                .retrieve()
                .body(CommentDto.class);
    }

    @Override
    public CommentDto updateComment(Long commentId, CommentDto commentDto) {
        return this.restClient.put()
                .uri("/comment/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(commentDto)
                .retrieve()
                .body(CommentDto.class);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) {
        return this.restClient.delete()
                .uri("/comment/{commentId}", commentId)
                .retrieve()
                .toBodilessEntity();
    }
}
