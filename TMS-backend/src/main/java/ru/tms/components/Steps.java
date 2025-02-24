package ru.tms.components;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tms.entity.Step;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Steps {

    List<Step> steps;
}