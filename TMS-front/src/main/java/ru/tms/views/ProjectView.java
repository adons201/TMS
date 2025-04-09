package ru.tms.views;

import com.vaadin.componentfactory.explorer.ExplorerTreeGrid;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.tms.dto.ProjectDto;
import ru.tms.dto.TestDto;
import ru.tms.services.CommentService;
import ru.tms.services.ProjectService;
import ru.tms.services.SuiteService;
import ru.tms.services.TestService;
import ru.tms.dto.SuiteDto;
import ru.tms.components.ReloadPage;
import ru.tms.components.CreateTestComponent;
import ru.tms.components.SuiteChildData;
import ru.tms.components.SuiteDiv;
import ru.tms.components.ViewAndEditTestComponent;
import ru.tms.models.ParentWebModel;
import ru.tms.models.SuiteWebModel;
import ru.tms.models.TestWebModel;
import org.vaadin.flow.helper.HasUrlParameterMapping;
import org.vaadin.flow.helper.UrlParameter;
import org.vaadin.flow.helper.UrlParameterMapping;

import java.util.*;

@Route(value = "project", layout = MainPage.class)
@UrlParameterMapping(":projectId")
@PageTitle("Project")
@CssImport("./styles/style.css")
@CssImport("./styles/shared-styles.css")
public class ProjectView extends VerticalLayout implements HasUrlParameterMapping, LocaleChangeObserver {

    private final Button createTestButton;
    private final HorizontalLayout removeAction;
    private final HorizontalLayout projectAction;
    private Button removeButton;
    private ProjectDto projectDto;
    private final ProjectService projectService;
    private final SuiteService suiteService;
    private final TestService testService;
    private final CommentService commentServiceClient;
    private List<SuiteDto> allSuites;
    private final VerticalLayout body = new VerticalLayout();
    private List<ParentWebModel> allHierarchy;
    private static final int ROW_HEIGHT_PX = 70;
    private static final int MAX_ROW = 10;

    public ProjectView(ProjectService projectService, SuiteService suiteService, TestService testService,
                       CommentService commentServiceClient) {
        this.projectService = projectService;
        this.suiteService = suiteService;
        this.testService = testService;
        this.commentServiceClient = commentServiceClient;
        setWidthFull();
        setHeightFull();
        Button createSuiteButton = createSuiteButton();
        createTestButton = createTestButton();
        removeAction = new HorizontalLayout();
        projectAction = new HorizontalLayout();
        projectAction.setAlignItems(Alignment.CENTER);
        Button backButton = new Button();
        Icon icon = VaadinIcon.ARROW_BACKWARD.create();
        icon.getStyle().set("padding", "0");
        icon.getStyle().set("width", "var(--lumo-icon-size-l)");
        icon.getStyle().set("height", "var(--lumo-icon-size-l)");
        icon.getStyle().set("margin-top", "1.25em");
        backButton.setIcon(icon);
        backButton.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(ProjectsView.class));
        projectAction.add(backButton);
        HorizontalLayout action = new HorizontalLayout();
        action.add(createSuiteButton, createTestButton, removeAction);
        add(projectAction, action);
        add(body);
    }

    @UrlParameter(name = "projectId")
    public void setProject(Long projectId) {
        this.projectDto = this.projectService.getProject(projectId);
        projectAction.add(new H2(getTranslation("project").concat(" id_" + this.projectDto.getId() + " "
                + this.projectDto.getTitle())));
        refresh();
    }

    private void refresh() {
        removeAction.removeAll();
        removeButton = new Button();
        removeButton.setVisible(false);
        removeAction.add(removeButton);
        allHierarchy = suiteService.getSuiteHierarchyNew(this.projectDto.getId());
        allSuites = new ArrayList<>(suiteService.getAllSuitesByProject(this.projectDto.getId()));
        createTestButton.setVisible(!allHierarchy.isEmpty());
        body.removeAll();
        TreeGrid<ParentWebModel> grid = buildGrid();
        body.add(grid);

        TextArea projectDescription = new TextArea(getTranslation("projectsDescription"));
        projectDescription.setValue(this.projectDto.getDescription());
        projectDescription.setReadOnly(true);
        projectDescription.getElement().getStyle().set("width", "45%");

        body.add(grid, projectDescription, new CommentView(commentServiceClient, "project",
                projectDto.getId(), "Adons201"));
    }

    private TreeGrid<ParentWebModel> buildGrid() {
        ExplorerTreeGrid<ParentWebModel> grid = new ExplorerTreeGrid<>();
        SuiteChildData suiteChildData = new SuiteChildData(allHierarchy);

        grid.addComponentHierarchyColumn(value -> {
            Icon icon = VaadinIcon.FOLDER_OPEN.create();
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setAlignItems(Alignment.CENTER);
            HorizontalLayout action;
            if (value instanceof TestWebModel) {
                icon = new Icon("lumo", "ordered-list");
                action = actionsTest((TestWebModel) value);
            } else {
                action = actionsSuite((SuiteWebModel) value);
            }
            horizontalLayout.addAndExpand(icon, new NativeLabel(value.getName()), action);
            return horizontalLayout;
        });

        int rowCount = suiteChildData.getRootChild().size()+1;
        grid.setHeight(rowCount * ROW_HEIGHT_PX + "px");
        grid.setMaxHeight(MAX_ROW * ROW_HEIGHT_PX + "px");
        grid.setItems(suiteChildData.getRootChild(), suiteChildData::getChildren);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.expand(suiteChildData.getRootChild());

        grid.asMultiSelect().addSelectionListener(selectionEvent -> {
            Set<ParentWebModel> allSelectedItems = selectionEvent.getAllSelectedItems();
            if (!allSelectedItems.isEmpty()) {
                removeButton.setText(String.format("Удалить %s выбранных элементов", allSelectedItems.size()));
            }
            removeButton.setVisible(!allSelectedItems.isEmpty());
        });
        removeButton.addClickListener(buttonClickEvent -> {
            if (removeButton.isVisible()) {
                Set<ParentWebModel> selectedItems = grid.asMultiSelect().getSelectedItems();
                ConfirmDialog confirmDialog = new ConfirmDialog(
                        String.format("%s %s %s",
                                getTranslation("notificationRemove"),
                                selectedItems.size(),
                                "элементов?"),
                        "",
                        getTranslation("remove"),
                        () -> {
                            deleteItems(selectedItems);
                            refresh();
                        });
                confirmDialog.open();
            }
        });
        return grid;
    }

    private void deleteItems(Set<ParentWebModel> selectedItems) {
        selectedItems.forEach(parentWebModel -> {
            if (parentWebModel instanceof TestWebModel) {
                TestDto test = ((TestWebModel) parentWebModel).getTest();
                testService.deleteTest(test.getId());
            } else {
                SuiteDto suite = ((SuiteWebModel) parentWebModel).getSuite();
                suiteService.deleteSuite(suite.getId());
            }
        });
        refresh();
    }

    private HorizontalLayout actionsSuite(SuiteWebModel suite) {
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(editSuiteButton(suite), deleteSuiteButton(suite));
        return actions;
    }

    private Button deleteSuiteButton(SuiteWebModel suite) {
        Button del = new Button(getTranslation("remove"));
        del.setIcon(VaadinIcon.TRASH.create());
        del.setId("del");
        del.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            ConfirmDialog confirmDialog = new ConfirmDialog(
                    getTranslation("notificationRemove")
                            + getTranslation("suite")
                            + suite.getSuite().getName() + " ?",
                    "",
                    getTranslation("remove"),
                    () -> {
                        suiteService.deleteSuite(suite.getSuite().getId());
                        refresh();
                    });
            confirmDialog.open();
        });
        return del;
    }

    private Button editSuiteButton(SuiteWebModel suite) {
        Button createSuite = new Button();
        createSuite.setIcon(VaadinIcon.EDIT.create());
        createSuite.setText(getTranslation("edit"));
        createSuite.addClickListener(buttonClickEvent -> {
            SuiteDto suiteDto = new SuiteDto();
            Binder<SuiteDto> binder = new Binder<>();
            VerticalLayout content = new VerticalLayout();
            TextField title = new TextField("Title");
            binder.forField(title).asRequired().bind(SuiteDto::getName, SuiteDto::setName);
            TextField description = new TextField("Description");
            binder.forField(description).bind(SuiteDto::getDescription, SuiteDto::setDescription);
            suiteDto.setProjectId(suite.getSuite().getProjectId());
            title.setValue(suite.getSuite().getName());
            description.setValue(suite.getSuite().getDescription());
            Select<SuiteDiv> select = new Select<>();
            select.setLabel("Parent suite");
            Collection<SuiteDiv> suiteList = new LinkedList<>();
            allSuites.forEach(x -> {
                suiteList.add(new SuiteDiv(x, suiteService.suiteAllParent(x.getProjectId(), x.getId())));
            });
            suiteList.removeIf(suiteDiv -> suiteDiv.getId().equals(suite.getSuite().getId()));
            suite.getChildrenSuites().forEach(x -> {
                suiteList.removeIf(suiteDiv -> suiteDiv.getId().equals(x.getSuite().getId()));
            });
            select.setItemLabelGenerator(SuiteDiv::getAllTitle);
            select.setItems(suiteList);
            if (suite.getSuite().getParentId() != null) {
                select.setValue(suiteList.stream().filter(x -> x.getId().equals(suite.getSuite().getParentId())).findFirst().get());
            }
            FormLayout gridLayout = new FormLayout();
            gridLayout.add(title, description, select);
            Button cancel = new Button(getTranslation("cancel"));
            Button save = new Button(getTranslation("save"));
            save.setIcon(VaadinIcon.PENCIL.create());
            content.add(gridLayout);
            HorizontalLayout hor = new HorizontalLayout();
            hor.add(cancel, save);
            content.add(hor);
            content.setAlignSelf(FlexComponent.Alignment.END, hor);
            Dialog window = new Dialog();
            window.add(content);
            window.setModal(true);
            cancel.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent1 -> window.close());
            save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent12 -> {
                binder.validate();
                if (binder.writeBeanIfValid(suiteDto)) {
                    if (select.getValue() != null) {
                        suiteDto.setParentId(select.getValue().getId());
                    }
                    suiteService.updateSuite(suite.getSuite().getId(), suiteDto);
                    window.close();
                    refresh();
                }
            });
            window.open();
        });
        return createSuite;
    }

    private HorizontalLayout actionsTest(TestWebModel test) {
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(viewTestButton(test), editTestButton(test), deleteTestButton(test));
        return actions;
    }

    private Button deleteTestButton(TestWebModel test) {
        Button del = new Button(getTranslation("remove"));
        del.setIcon(VaadinIcon.TRASH.create());
        del.setId("del");
        del.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
            ConfirmDialog confirmDialog = new ConfirmDialog(
                    getTranslation("notificationRemove")
                            + getTranslation("test")
                            + test.getName() + " ?",
                    "",
                    getTranslation("remove"),
                    () -> {
                        testService.deleteTest(test.getTest().getId());
                        refresh();
                    });
            confirmDialog.open();
        });
        return del;
    }

    private Button editTestButton(TestWebModel test) {
        Button editTest = new Button();
        editTest.setIcon(VaadinIcon.EDIT.create());
        editTest.setText(getTranslation("edit"));
        editTest.addClickListener(buttonClickEvent ->
                new ViewAndEditTestComponent(test.getTest(), suiteService, testService, this::refresh, true));
        return editTest;
    }

    private Button viewTestButton(TestWebModel test) {
        Button editTest = new Button();
        editTest.setIcon(VaadinIcon.VIEWPORT.create());
        editTest.setText(getTranslation("view"));
        editTest.addClickListener(buttonClickEvent ->
                new ViewAndEditTestComponent(test.getTest(), suiteService, testService, this::refresh, false));
        return editTest;
    }

    private Button createSuiteButton() {
        Button createSuite = new Button();
        createSuite.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        createSuite.setText(getTranslation("createSuite"));
        createSuite.addClickListener(buttonClickEvent -> {
            SuiteDto suiteDto = new SuiteDto();
            Binder<SuiteDto> binder = new Binder<>();
            VerticalLayout content = new VerticalLayout();
            TextField title = new TextField("Title");
            binder.forField(title).asRequired().bind(SuiteDto::getName, SuiteDto::setName);
            TextField description = new TextField("Description");
            binder.forField(description).bind(SuiteDto::getDescription, SuiteDto::setDescription);
            suiteDto.setProjectId(this.projectDto.getId());
            Select<SuiteDiv> select = new Select<>();
            select.setLabel("Parent suite");
            List<SuiteDiv> suiteList = new LinkedList<>();
            suiteService.getAllSuitesByProject(this.projectDto.getId())
                    .forEach(x -> {
                        suiteList.add(new SuiteDiv(x, suiteService.suiteAllParent(x.getProjectId(), x.getId())));
                    });
            select.setItemLabelGenerator(SuiteDiv::getAllTitle);
            select.setItems(suiteList);
            FormLayout gridLayout = new FormLayout();
            gridLayout.add(title, description, select);
            Button cancel = new Button(getTranslation("cancel"));
            Button save = new Button(getTranslation("save"));
            save.setIcon(VaadinIcon.PENCIL.create());
            content.add(gridLayout);
            HorizontalLayout hor = new HorizontalLayout();
            hor.add(cancel, save);
            content.add(hor);
            content.setAlignSelf(FlexComponent.Alignment.END, hor);
            Dialog window = new Dialog();
            window.add(content);
            window.setModal(true);
            cancel.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent1 -> window.close());
            save.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent12 -> {
                binder.validate();
                if (binder.writeBeanIfValid(suiteDto)) {
                    if (select.getValue() != null) {
                        suiteDto.setParentId(select.getValue().getId());
                    }
                    suiteService.createSuite(suiteDto);
                    window.close();
                    refresh();
                }
            });
            window.open();
        });
        return createSuite;
    }

    private Button createTestButton() {
        Button createTest = new Button();
        createTest.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        createTest.setText(getTranslation("createTest"));
        createTest.addClickListener(buttonClickEvent -> {
            CreateTestComponent createTestComponent = new CreateTestComponent(this.projectDto.getId(), suiteService
                    , testService, this::refresh);
        });
        return createTest;
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        ReloadPage.reloadPage(localeChangeEvent, this.getClass());
    }
}