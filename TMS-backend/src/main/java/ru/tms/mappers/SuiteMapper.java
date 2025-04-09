package ru.tms.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.tms.dto.SuiteDto;
import ru.tms.entity.Suite;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SuiteMapper {

    @Mapping(target = "projectId", expression = "java(suite.getProject().getId())")
    @Mapping(target = "parentId", expression = "java(suite.getParentId() != null ? suite.getParentId().getId() : null)")
    SuiteDto toDto(Suite suite);
    List<SuiteDto> toDto(List<Suite> suites);
}
