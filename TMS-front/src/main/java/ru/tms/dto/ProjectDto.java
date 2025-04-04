package ru.tms.front.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ProjectDto {

    private Long id;
    private String title;
    private String description;
}
