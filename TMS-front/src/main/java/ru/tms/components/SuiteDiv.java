package ru.tms.components;


import lombok.Data;

@Data
public class SuiteDiv {

    private String allTitle;
    private Suite suite;

    public SuiteDiv(Suite suite) {
        this.suite = suite;
        id = suite.getId();
        title = suite.getName();
        description = suite.getDescription();
        projectId = suite.getProject();
        parentId = suite.getParentId();
        allTitle = "";
        if (parentId != null) {
            setTitles(parentId);
            char uniChar = '\u21b3';
            allTitle += uniChar + " " + title;

        } else {
            allTitle = title;
        }
    }

    private Long id;

    private String title;

    private String description;

    private Project projectId;

    private Suite parentId;

    private void setTitles(Suite parentSuite) {
        if (parentSuite != null) {
            this.allTitle += " | ";
            setTitles(parentSuite.getParentId());
        }
    }
}
