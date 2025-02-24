package ru.tms.dto;

import lombok.Data;
import ru.tms.components.Step;

import java.util.List;

@Data
public class TestDto {

    private String title;
    private String status;
    private String description;
    private Long suiteId;
    private Long projectId;
    private Boolean automated;
    private List<Step> steps;

}