package ru.tms.dto;

public record Subscription(Long id, String userId, String targetType, Long targetObjectId) {}