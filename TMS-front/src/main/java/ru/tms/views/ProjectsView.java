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
import ru.tms.dto.Project;
import ru.tms.components.ReloadPage;

@CssImport("./styles/shared-styles.css")
@Route(value = "projects", layout = MainPage.class)
@PageTitle("Projects")
public class ProjectsView extends VerticalLayout implements LocaleChangeObserver {

    private final ProjectService projectServiceClient;
    private Grid<Project> grid;

    public ProjectsView(ProjectService projectServiceClient) {
        this.projectServiceClient = projectServiceClient;
        init();
    }

    public void init() {
        removeAll();
        setSizeFull();
        add(new H2(getTranslation("projects")), createProjectButton());
        grid2();
        add(grid);
    }

    public void grid2() {
        grid = new Grid<>(Project.class, false);
        refresh();
        grid.addColumn("id").setWidth("75px").setFlexGrow(0);
        grid.addColumn("title");
        grid.addColumn("description");
        grid.addComponentColumn(project -> createEditButton(project)).setKey("edit");
        grid.addComponentColumn(project -> createRemoveButton(project)).setKey("remove");
        grid.setColumnReorderingAllowed(false);
        grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<Project>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<Project> projectItemClickEvent) {
                UI.getCurrent().navigate(ProjectView.class,
                        projectItemClickEvent.getItem().getId().toString());
            }
        });
    }

    private Button createRemoveButton(Project project) {
        @SuppressWarnings("unchecked")
        Button button = new Button(getTranslation("remove"), clickEvent -> {
            ConfirmDialog confirmDialog = new ConfirmDialog(
                    getTranslation("notificationRemove")
                            + getTranslation("project")
                            + project.getTitle() + " ?",
                    "",
                    getTranslation("remove"),
                    () -> {
                        projectServiceClient.deleteProject(project.getId());
                        refresh();
                    });
            confirmDialog.open();
        });
        button.setIcon(VaadinIcon.TRASH.create());
        button.setId("del");
        return button;
    }

    private Button createEditButton(Project project) {
        @SuppressWarnings("unchecked")
        Button button = new Button(getTranslation("edit"), clickEvent -> {
            Project projectDto = Project.builder().build();
            Binder<Project> binder = new Binder<>();
            VerticalLayout content = new VerticalLayout();
            TextField title = new TextField("Title");
            title.setValue(project.getTitle());
            binder.forField(title).asRequired().bind(Project::getTitle, Project::setTitle);
            TextField description = new TextField("Description");
            description.setValue(project.getDescription());
            binder.forField(description).bind(Project::getDescription, Project::setDescription);
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
                        projectServiceClient.updateProject(project.getId(), projectDto);
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
            Project project = Project.builder().build();
            Binder<Project> binder = new Binder<>();
            VerticalLayout content = new VerticalLayout();
            TextField title = new TextField("Title");
            binder.forField(title).asRequired().bind(Project::getTitle, Project::setTitle);
            TextField description = new TextField("Description");
            binder.forField(description).bind(Project::getDescription, Project::setDescription);
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
                    if (binder.writeBeanIfValid(project)) {
                        projectServiceClient.createProject(project);
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
        grid.setItems(projectServiceClient.getProjects());
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        ReloadPage.reloadPage(localeChangeEvent, this.getClass());
    }
}
