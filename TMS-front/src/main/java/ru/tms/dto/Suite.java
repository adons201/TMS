package ru.tms.dto;

import lombok.Data;

@Data
public class Suite {

    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private Long parentId;
}
