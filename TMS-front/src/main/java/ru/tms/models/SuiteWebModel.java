package ru.tms.models;

import lombok.Getter;
import ru.tms.components.Suite;

import java.util.ArrayList;
import java.util.List;

public class SuiteWebModel implements ParentWebModel{

    @Getter
    private Suite suite;
    private List<ParentWebModel> children;

    public SuiteWebModel(Suite suite, List<ParentWebModel> children) {
        this.suite = suite;
        this.children = children;
    }

    public List<SuiteWebModel> getChildrenSuites() {
        List<SuiteWebModel> result = new ArrayList<>();
        getChildren().forEach(parentWebModel -> {
            if (parentWebModel instanceof SuiteWebModel) {
                result.add((SuiteWebModel) parentWebModel);
            }
        });
        return result;
    }

    @Override
    public String getName() {
        return suite.getName();
    }

    @Override
    public List<ParentWebModel> getChildren() {
        return children;
    }
}
