package ru.tms.dto;

import lombok.Data;

import java.util.List;

@Data
public class Test {

    private Long id;
    private String title;
    private String status;
    private String description;
    private Long suiteId;
    private Long projectId;
    private Boolean automated;
    private List<Step> steps;

}