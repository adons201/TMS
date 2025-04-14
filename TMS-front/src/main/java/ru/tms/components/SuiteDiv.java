package ru.tms.components;


import lombok.Data;
import ru.tms.dto.Suite;

import java.util.List;

@Data
public class SuiteDiv {

    private String allTitle;
    private Suite suite;
    private Long id;
    private String title;
    private String description;
    private Long projectId;
    private Long parentId;

    public SuiteDiv(Suite suite, List<Suite> suitesParent) {
        this.suite = suite;
        id = suite.getId();
        title = suite.getName();
        description = suite.getDescription();
        projectId = suite.getProjectId();
        parentId = suite.getParentId();
        allTitle = "";
        if (parentId != null && !suitesParent.isEmpty()) {
            setTitles(suitesParent);
            char uniChar = '\u21b3';
            allTitle += uniChar + " " + title;

        } else {
            allTitle = title;
        }
    }

    private void setTitles(List<Suite> suitesParent) {
        for(Suite suite : suitesParent){
            this.allTitle += " | ";
        }
    }
}
