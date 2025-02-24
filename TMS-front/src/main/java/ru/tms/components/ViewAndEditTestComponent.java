package ru.tms.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import ru.tms.components.vaadin.maxime.MarkdownArea;
import ru.tms.services.SuiteService;
import ru.tms.services.TestService;
import ru.tms.dto.TestDto;
import ru.tms.converters.ConvertSuiteDivToSuiteDto;

import java.util.*;

@CssImport("./styles/shared-styles.css")
public class ViewAndEditTestComponent extends Dialog {

    private VerticalLayout verticalStep = new VerticalLayout();
    private Binder<TestDto> binder;
    private TestDto testDto;
    int nextStep = 1;
    private HorizontalLayout action;
    Select<SuiteDiv> select;
    TestService testService;
    SuiteService suiteService;
    private HorizontalLayout hor;
    private Map<Step, Binder> stepBinderMap;
    private Runnable actionClose;

    private Test test;

    /**
     * @param test
     * @param suiteService
     * @param testService
     * @param actionClose
     */
    public ViewAndEditTestComponent(Test test, SuiteService suiteService, TestService testService,
                                    Runnable actionClose, Boolean isEditable) {
        String titleHeader = getTranslation("viewTest");
        if (isEditable) {
            titleHeader = getTranslation("editTest");
        }
        this.test = test;
        this.suiteService = suiteService;
        this.testService = testService;
        this.actionClose = actionClose;
        this.stepBinderMap = new LinkedHashMap<>();
        setSizeFull();
        getElement().setAttribute("aria-label", String.format("%s %s", titleHeader, test.getTitle()));
        getElement().getStyle().set("scrolling", "auto");
        testDto = new TestDto();
        testDto.setTitle(test.getTitle());
        testDto.setDescription(test.getDescription());
        testDto.setStatus(test.getStatus());
        testDto.setSuiteId(test.getSuiteId().getId());
        testDto.setProjectId(test.getProjectId().getId());
        testDto.setAutomated(test.getAutomated());
        testDto.setSteps(test.getSteps());
        binder = new Binder<>();
        VerticalLayout content = new VerticalLayout();
        TextField title = new TextField("Title");
        title.setRequired(true);
        binder.forField(title)
                .withValidator(new StringLengthValidator(getTranslation("fill"), 1, 200))
                .bind(TestDto::getTitle, TestDto::setTitle);
        TextField description = new TextField("Description");
        binder.forField(description).bind(TestDto::getDescription, TestDto::setDescription);
        testDto.setProjectId(test.getProjectId().getId());
        select = new Select<>();
        select.setRequiredIndicatorVisible(true);
        select.setLabel("Parent Suite");
        List<SuiteDiv> suiteList = new LinkedList<>();
        suiteService.getAllSuitesByProject(test.getProjectId().getId())
                .forEach(x -> {
                    suiteList.add(new SuiteDiv(x));
                });
        select.setItemLabelGenerator(SuiteDiv::getAllTitle);
        select.setItems(suiteList);
        binder.forField(select).withConverter(new ConvertSuiteDivToSuiteDto()).asRequired().bind(TestDto::getSuiteId, TestDto::setSuiteId);
        Checkbox isAutomated = new Checkbox();
        isAutomated.setLabel("Automated");
        binder.forField(isAutomated).bind(TestDto::getAutomated, TestDto::setAutomated);
        TextField status = new TextField("Status");
        binder.forField(status).bind(TestDto::getStatus, TestDto::setStatus);
        action = new HorizontalLayout();
        Button createStep = new Button(getTranslation("createStep"));
        if (isEditable) {
            action.add(createStep);
        }
        createStep.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                addStep();
            }
        });
        FormLayout gridLayout = new FormLayout();
        gridLayout.setWidthFull();
        gridLayout.add(select, 0);
        gridLayout.add(title, 1);
        gridLayout.add(description, 3);
        gridLayout.add(isAutomated, 4);
        gridLayout.add(status, 5);
        gridLayout.add(verticalStep, 6);
        gridLayout.add(action, 7);
        content.add(gridLayout);
        add(content);
        cancelSave(isEditable);
        binder.setBean(testDto);
        select.setValue(suiteList.stream().filter(x -> x.getId().equals(test.getSuiteId().getId())).findFirst().get());
        testDto.getSteps().forEach(step -> {
            renderStep(step, isEditable);
        });
        stepBinderMap.forEach((step, binder1) -> {
            binder1.setBean(step);
        });
        fieldEditable(isEditable, title, description, select, isAutomated, status);
        open();
    }

    private void cancelSave(Boolean isEditable) {
        Button cancel = new Button(isEditable ? getTranslation("cancel") : getTranslation("close"));
        Button save = new Button(getTranslation("save"));
        save.setVisible(isEditable);
        save.setIcon(VaadinIcon.PENCIL.create());
        cancel.addClickListener(buttonClickEvent -> {
            close();
            actionClose.run();
        });
        save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                List<Step> steps = new LinkedList<>();
                for (Map.Entry<Step, Binder> entry : stepBinderMap.entrySet()) {
                    entry.getValue().validate();
                    if (entry.getValue().writeBeanIfValid(entry.getKey())) {
                        steps.add(entry.getKey());
                    }
                }
                testDto.setSteps(steps);
                binder.validate();
                if (binder.writeBeanIfValid(testDto)) {
                    testService.updateTest(test.getId(), testDto);
                    close();
                    actionClose.run();
                }
            }
        });
        hor = new HorizontalLayout();
        hor.add(cancel, save);
        hor.setId("actions");
        add(hor);
    }

    private void renderStep(Step step, Boolean isEditable) {
        Binder<Step> stepBinder = new Binder<>();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new NativeLabel(getTranslation("step") + step.getNumber()));
        verticalLayout.getStyle().set("border", "1px solid #e5e5e5");
        HorizontalLayout name = new HorizontalLayout();
        name.setWidthFull();
        NativeLabel nameAction = new NativeLabel(getTranslation("action"));
        nameAction.getElement().getStyle().set("width", "50%");
        NativeLabel nameResult = new NativeLabel(getTranslation("expectedResult"));
        nameResult.getElement().getStyle().set("width", "50%");
        name.add(nameAction, nameResult);
        verticalLayout.add(name);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        MarkdownArea action = new MarkdownArea();
        action.getElement().getStyle().set("width", "50%");
        MarkdownArea result = new MarkdownArea();
        result.getElement().getStyle().set("width", "50%");
        horizontalLayout.add(action);
        horizontalLayout.add(result);
        action.editable(isEditable, step.getAction());
        result.editable(isEditable, step.getExpectedResult());
        if (isEditable) {
            stepBinder.forField(action.getInput()).bind(Step::getAction, Step::setAction);
            stepBinder.forField(result.getInput()).bind(Step::getExpectedResult, Step::setExpectedResult);
            stepBinderMap.put(step, stepBinder);

        }
        nextStep = step.getNumber() + 1;
        verticalLayout.add(horizontalLayout);
        add(verticalLayout);
        add(this.action);
    }

    private void addStep() {
        Step step = new Step();
        step.setNumber(nextStep);
        Binder<Step> stepBinder = new Binder<>();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new NativeLabel(getTranslation("step") + nextStep));
        verticalLayout.getStyle().set("border", "1px solid #e5e5e5");
        HorizontalLayout name = new HorizontalLayout();
        name.setWidthFull();
        NativeLabel nameAction = new NativeLabel(getTranslation("action"));
        nameAction.getElement().getStyle().set("width", "50%");
        NativeLabel nameResult = new NativeLabel(getTranslation("expectedResult"));
        nameResult.getElement().getStyle().set("width", "50%");
        name.add(nameAction, nameResult);
        verticalLayout.add(name);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        MarkdownArea action = new MarkdownArea();
        action.getElement().getStyle().set("width", "50%");
        MarkdownArea result = new MarkdownArea();
        result.getElement().getStyle().set("width", "50%");
        horizontalLayout.add(action);
        horizontalLayout.add(result);
        stepBinder.forField(action.getInput()).bind(Step::getAction, Step::setAction);
        stepBinder.forField(result.getInput()).bind(Step::getExpectedResult, Step::setExpectedResult);
        stepBinderMap.put(step, stepBinder);
        nextStep++;
        verticalLayout.add(horizontalLayout);
        add(verticalLayout);
        add(this.action);
    }

    private void fieldEditable(Boolean isEditable, Component... components) {
        if (!isEditable) {
            for (Component component : components) {
                if (component instanceof Checkbox) {
                    component.getElement().setAttribute("onClick", "return false;");
                } else {
                    component.getElement().setAttribute("readonly", "readonly");
                }
            }
        }
    }
}