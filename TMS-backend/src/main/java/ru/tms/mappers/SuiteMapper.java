package ru.tms.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tms.dto.Suite;
import ru.tms.entity.SuiteEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SuiteMapper {

    @Mapping(target = "projectId", expression = "java(suiteEntity.getProject().getId())")
    @Mapping(target = "parentId",
            expression = "java(suiteEntity.getParentId() != null ? suiteEntity.getParentId().getId() : null)")
    Suite toDto(SuiteEntity suiteEntity);
    List<Suite> toDto(List<SuiteEntity> suiteEntity);
}
