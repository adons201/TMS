package ru.tms.dto;

import java.time.Instant;

public record Comment(Long id, String content, Instant createdAt, String author,
                      String targetType, Long targetObjectId, Boolean changed) {}