package ru.tms.dto;

import java.time.LocalDateTime;

public record CommentDto(Long id, String content, LocalDateTime createdAt, String author,
                         String targetType, Long targetObjectId, Boolean changed) {
}
