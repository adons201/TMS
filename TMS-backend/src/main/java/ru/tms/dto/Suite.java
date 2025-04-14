package ru.tms.dto;

public record Suite(Long id, String name, String description, Long projectId, Long parentId) {}
