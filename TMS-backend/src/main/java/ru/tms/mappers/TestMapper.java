package ru.tms.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tms.dto.TestDto;
import ru.tms.entity.Test;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestMapper {

    @Mapping(target = "suiteId", expression = "java(test.getSuiteId().getId())")
    @Mapping(target = "projectId", expression = "java(test.getProjectId().getId())")
    TestDto toDto(Test test);
    List<TestDto> toDto(List<Test> tests);
}
