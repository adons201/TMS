package ru.tms.components;

import lombok.Getter;
import ru.tms.models.ParentWebModel;
import ru.tms.models.SuiteWebModel;

import java.util.ArrayList;
import java.util.List;

public class SuiteChildData {

    @Getter
    private final List<ParentWebModel> rootChild;

    public SuiteChildData(List<ParentWebModel> rootChild) {
        this.rootChild = new ArrayList<>();
        rootChild.forEach(parentWebModel -> {
            if (parentWebModel instanceof SuiteWebModel) {
                this.rootChild.add(parentWebModel);
            }
        });
    }

    public List<ParentWebModel> getChildren(ParentWebModel parent) {
        if (parent != null) {
            return parent.getChildren();
        }
        else {
            return new ArrayList<>();
        }
    }
}
