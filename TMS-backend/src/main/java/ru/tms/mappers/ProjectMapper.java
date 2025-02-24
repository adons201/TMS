package ru.tms.mappers;


import org.mapstruct.Mapper;
import ru.tms.entity.Project;
import ru.tms.dto.ProjectDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDto toDto(Project project);
    List<ProjectDto> toDto(List<Project> projects);
    Project toEntity(ProjectDto projectDto);
}
