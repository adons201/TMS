package ru.tms.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tms.dto.Test;
import ru.tms.entity.TestEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestMapper {

    @Mapping(target = "suiteId", expression = "java(testEntity.getSuiteId().getId())")
    @Mapping(target = "projectId", expression = "java(testEntity.getProjectId().getId())")
    Test toDto(TestEntity testEntity);
    List<Test> toDto(List<TestEntity> testEntity);
}
