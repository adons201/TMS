package ru.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Project {

    private Long id;
    private String title;
    private String description;
}