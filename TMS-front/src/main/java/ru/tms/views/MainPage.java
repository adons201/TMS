package ru.tms.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.tms.components.ReloadPage;

import java.util.Arrays;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@CssImport("./styles/shared-styles.css")
@PageTitle("TMS")
@Route("")
public class MainPage extends AppLayout implements LocaleChangeObserver
{

    private final Tabs menu;
    private H1 viewTitle;
    private Button logoutButton;
    private HorizontalLayout header;
    private Component headerContent;
    private UserDetailsService userService;

    public MainPage() {
        //setPrimarySection(Section.DRAWER);
        setPrimarySection(Section.NAVBAR);
        headerContent = createHeaderContent();
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
        this.userService = userService;
    }

    private Component createHeaderContent() {
        header = new HorizontalLayout();
        header.setId("header");
        header.setWidthFull();
        header.setSpacing(false);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.add(new DrawerToggle());
        viewTitle = new H1();
        header.add(viewTitle);
        logoutButton = createMenuButton(getTranslation("logout"), VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(e -> logout());
        logoutButton.getElement().setAttribute("title", getTranslation("logout") + " (Ctrl+L)");
        return header;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image logo = new Image("icons/icon.png", "Logo");
        logo.setId("logo");
        logo.setWidth("100%");
        logo.setHeight("100%");
        logoLayout.add(logo);
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        final Component[] components = createMenuItems();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_SMALL);
        Tab[] tabsComponent = (Tab[])Arrays.copyOf(components, components.length, Tab[].class);
        tabs.add(tabsComponent);
        return tabs;
    }

    private Component[] createMenuItems() {
        RouterLink[] links = new RouterLink[]{
                new RouterLink(getTranslation("projects"), ProjectsView.class)
        };
        return Arrays.stream(links).map(MainPage::createTab).toArray(Tab[]::new);
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    @Override
    public void afterNavigation() {
        super.afterNavigation();
        updateChrome();
    }

    private void updateChrome() {
        getTabWithCurrentRoute().ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabWithCurrentRoute() {
        String currentRoute = RouteConfiguration.forSessionScope()
                .getUrl(getContent().getClass());
        return menu.getChildren().filter(tab -> hasLink(tab, currentRoute))
                .findFirst().map(Tab.class::cast);
    }

    private boolean hasLink(Component tab, String currentRoute) {
        return tab.getChildren().filter(RouterLink.class::isInstance)
                .map(RouterLink.class::cast).map(RouterLink::getHref)
                .anyMatch(currentRoute::equals);
    }

    @SneakyThrows
    private String getCurrentPageTitle() {
        PageTitle pageTitle = getContent().getClass().getAnnotation(PageTitle.class);
        if (pageTitle == null) {
            return "";
        } else return getContent().getClass().getAnnotation(PageTitle.class).value();
    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("24px");
        return routerButton;
    }

    private void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // User can quickly activate logout with Ctrl+L
        attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L, KeyModifier.CONTROL);

        //Button profileButton = createMenuButton(getTranslation("profile"), VaadinIcon.USER.create());
        //profileButton.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(ProfileView.class));
        //ChangeThemeComponent changeThemeComponent = new ChangeThemeComponent(headerContent);
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        //Users userByEmail = userService.getUserByEmail(SecurityUtils.getLoggedUser().getUsername());
        //Avatar avatar = new Avatar(userByEmail.getFullName());
        /*
        if (userByEmail.getImage() != null) {
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(Base64.decodeBase64(userByEmail.getImage())));
            avatar.setImageResource(resource);
        }
        */

        //avatar.setThemeName("xsmall");
        //avatar.getElement().setAttribute("tabindex", "-1");
        Div div = new Div();
        //div.add(avatar);
        //div.add(userByEmail.getFullName());
        div.add(new Icon("lumo", "dropdown"));
        div.getElement().getStyle().set("display", "flex");
        div.getElement().getStyle().set("align-items", "center");
        div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
        MenuItem menuItem = menuBar.addItem(div);

        SubMenu subMenu = menuItem.getSubMenu();
        //subMenu.addItem(profileButton);
        subMenu.addItem(logoutButton);
        addToNavbar(/*changeThemeComponent,*/ /*new LanguageSelectView().getLangSelect(),*/ menuBar);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        ReloadPage.reloadPage(localeChangeEvent, this.getClass());
    }
}