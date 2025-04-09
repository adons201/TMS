package ru.tms.dto;

import ru.tms.entity.Step;

import java.util.List;

public record TestDto (Long id, String title, String status, String description, Long suiteId
        , Long projectId, Boolean automated, List<Step> steps) {}