package ru.tms.components;

import lombok.Data;

@Data
public class Step {

    private Integer number;
    private String action;
    private String expectedResult;

}
