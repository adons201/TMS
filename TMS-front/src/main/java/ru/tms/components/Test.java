package ru.tms.components;

import lombok.Data;

import java.util.List;

@Data
public class Test {

    private Long id;
    private String title;
    private String status;
    private String description;
    private Suite suiteId;
    private Project projectId;
    private Boolean automated;
    private List<Step> steps;
}
