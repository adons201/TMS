package ru.tms.dto;

public record ReducedComment(Long id,String content, String targetType, Long targetObjectId) {}