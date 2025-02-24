package ru.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuiteDto {

    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private Long parentId;
}
