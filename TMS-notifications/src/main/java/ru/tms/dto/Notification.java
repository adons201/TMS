package ru.tms.dto;

import java.time.LocalDateTime;

public record Notification(Long id, String content, LocalDateTime createAt, String userId, Boolean read) {}