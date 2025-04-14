package ru.tms.mappers;


import org.mapstruct.Mapper;
import ru.tms.entity.ProjectEntity;
import ru.tms.dto.Project;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toDto(ProjectEntity projectEntity);
    List<Project> toDto(List<ProjectEntity> projectEntities);
    ProjectEntity toEntity(Project project);
}
