package ru.tms.models;

import lombok.Getter;
import ru.tms.dto.TestDto;

import java.util.List;

public class TestWebModel implements ParentWebModel{

    @Getter
    private TestDto test;

    public TestWebModel(TestDto test) {
        this.test = test;
    }

    @Override
    public String getName() {
        return test.getTitle();
    }

    @Override
    public List<ParentWebModel> getChildren() {
        return List.of();
    }
}
