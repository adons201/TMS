package ru.tms.dto;

public record SuiteDto (Long id, String name, String description, Long projectId, Long parentId) {}
