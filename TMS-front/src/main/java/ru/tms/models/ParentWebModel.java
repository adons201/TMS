package ru.tms.models;

import java.util.List;

public interface ParentWebModel {

    String getName();

    List<ParentWebModel> getChildren();
}
