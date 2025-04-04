package ru.tms.front.dto;

import lombok.Data;

@Data
public class SuiteDto {

    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private Long parentId;
}
