package ru.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class Comment {
    private Long id;
    private String content;
    private Instant createdAt;
    private String author;
    private String targetType;
    private Long targetObjectId;
    private Boolean changed;
}
