package ru.tms.components;

import lombok.Data;

@Data
public class Suite {
    private Long id;
    private String name;
    private String description;
    private Project project;
    private Suite parentId;

}
