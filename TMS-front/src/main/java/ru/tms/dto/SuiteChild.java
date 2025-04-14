package ru.tms.dto;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class SuiteChild {

    private String name;
    private Suite suite;
    private List<SuiteChild> childSuites;
    private List<Suite> allChildren;
    private Collection<Test> tests;

    public SuiteChild(Suite suite, List<SuiteChild> childSuites, List<Suite> allChildren, Collection<Test> tests) {
        this.suite = suite;
        this.childSuites = childSuites;
        this.allChildren = allChildren;
        this.tests = tests;
        this.name = suite.getName();
    }
}
