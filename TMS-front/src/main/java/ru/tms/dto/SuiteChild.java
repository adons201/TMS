package ru.tms.dto;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class SuiteChild {

    private String name;
    private SuiteDto suite;
    private List<SuiteChild> childSuites;
    private List<SuiteDto> allChildren;
    private Collection<TestDto> tests;

    public SuiteChild(SuiteDto suite, List<SuiteChild> childSuites,List<SuiteDto> allChildren, Collection<TestDto> tests) {
        this.suite = suite;
        this.childSuites = childSuites;
        this.allChildren = allChildren;
        this.tests = tests;
        this.name = suite.getName();
    }
}
