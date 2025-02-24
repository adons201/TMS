package ru.tms.models;

import lombok.Getter;
import ru.tms.components.Test;

import java.util.List;

public class TestWebModel implements ParentWebModel{

    @Getter
    private Test test;

    public TestWebModel(Test test) {
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
