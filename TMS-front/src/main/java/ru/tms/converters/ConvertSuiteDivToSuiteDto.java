package ru.tms.converters;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import ru.tms.dto.Suite;
import ru.tms.components.SuiteDiv;

public class ConvertSuiteDivToSuiteDto implements Converter<SuiteDiv, Long> {

    Suite suite;
    SuiteDiv suiteDiv;

    @Override
    public Result<Long> convertToModel(SuiteDiv suiteDiv, ValueContext valueContext) {
        if (valueContext==null){
            return Result.ok(null);
        }
        try {
            this.suiteDiv = suiteDiv;
            suite = new Suite();
            suite.setParentId(suiteDiv.getSuite().getId());
            return Result.ok(suite.getParentId());
        } catch (Exception e) {
            return Result.error("Selected value");
        }
    }

    @Override
    public SuiteDiv convertToPresentation(Long aLong, ValueContext valueContext) {
        return this.suiteDiv;
    }

}
