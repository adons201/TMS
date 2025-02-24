package ru.tms.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.tms.services.ProjectService;
import ru.tms.dto.ProjectDto;
import ru.tms.components.ReloadPage;

@CssImport("./styles/shared-styles.css")
@Route(value = "projects", layout = MainPage.class)
@PageTitle("Projects")
public class ProjectsView extends VerticalLayout implements LocaleChangeObserver {

    private final ProjectService projectService;
    private Grid<ProjectDto> grid;

    public ProjectsView(ProjectService projectService) {
        this.projectService = projectService;
        init();
    }

    public void init() {
        removeAll();
        setSizeFull();
        add(new H2("Проекты"), createProjectButton());
        grid2();
        add(grid);
    }

    public void grid2() {
        grid = new Grid<>(ProjectDto.class, false);
        refresh();
        grid.addColumn("id").setWidth("75px").setFlexGrow(0);
        grid.addColumn("title");
        grid.addColumn("description");
        grid.addComponentColumn(project -> createEditButton(project)).setKey("edit");
        grid.addComponentColumn(project -> createRemoveButton(project)).setKey("remove");
        grid.setColumnReorderingAllowed(false);
        grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<ProjectDto>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<ProjectDto> projectItemClickEvent) {
                UI.getCurrent().navigate(ProjectView.class,
                        projectItemClickEvent.getItem().getId().toString());
            }
        });
    }

    private Button createRemoveButton(ProjectDto project) {
        @SuppressWarnings("unchecked")
        Button button = new Button(getTranslation("remove"), clickEvent -> {
            ConfirmDialog confirmDialog = new ConfirmDialog(
                    getTranslation("notificationRemove")
                            + getTranslation("project")
                            + project.getTitle() + " ?",
                    "",
                    getTranslation("remove"),
                    () -> {
                        projectService.deleteProject(project.getId());
                        refresh();
                    });
            confirmDialog.open();
        });
        button.setIcon(VaadinIcon.TRASH.create());
        button.setId("del");
        return button;
    }

    private Button createEditButton(ProjectDto project) {
        @SuppressWarnings("unchecked")
        Button button = new Button(getTranslation("edit"), clickEvent -> {
            ProjectDto projectDto = ProjectDto.builder().build();
            Binder<ProjectDto> binder = new Binder<>();
            VerticalLayout content = new VerticalLayout();
            TextField title = new TextField("Title");
            title.setValue(project.getTitle());
            binder.forField(title).asRequired().bind(ProjectDto::getTitle, ProjectDto::setTitle);
            TextField description = new TextField("Description");
            description.setValue(project.getDescription());
            binder.forField(description).bind(ProjectDto::getDescription, ProjectDto::setDescription);
            FormLayout gridLayout = new FormLayout();
            gridLayout.add(title, description);
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
            cancel.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    window.close();
                }
            });
            save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    binder.validate();
                    if (binder.writeBeanIfValid(projectDto)) {
                        projectService.updateProject(project.getId(), projectDto);
                        window.close();
                        refresh();
                    }
                }
            });
            window.open();
        });
        button.setIcon(VaadinIcon.EDIT.create());
        return button;
    }

    private Button createProjectButton() {
        Button createProject = new Button();
        createProject.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        createProject.setText(getTranslation("createProject"));
        createProject.addClickListener(buttonClickEvent -> {
            ProjectDto projectDto = ProjectDto.builder().build();
            Binder<ProjectDto> binder = new Binder<>();
            VerticalLayout content = new VerticalLayout();
            TextField title = new TextField("Title");
            binder.forField(title).asRequired().bind(ProjectDto::getTitle, ProjectDto::setTitle);
            TextField description = new TextField("Description");
            binder.forField(description).bind(ProjectDto::getDescription, ProjectDto::setDescription);
            FormLayout gridLayout = new FormLayout();
            gridLayout.add(title, description);
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
            cancel.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    window.close();
                }
            });
            save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    binder.validate();
                    if (binder.writeBeanIfValid(projectDto)) {
                        projectService.createProject(projectDto);
                        window.close();
                        refresh();
                    }
                }
            });
            window.open();
        });
        return createProject;
    }

    void refresh() {
        grid.setItems(projectService.getAllProjects());
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        ReloadPage.reloadPage(localeChangeEvent, this.getClass());
    }
}
