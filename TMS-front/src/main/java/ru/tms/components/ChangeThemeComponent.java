package ru.tms.components;

//import com.gmail.umit.PaperToggleButton;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.Lumo;

import jakarta.servlet.http.Cookie;

public class ChangeThemeComponent extends HorizontalLayout {
    private static final String LIGHTMODE = "lightmode";
    private final Component headerComponent;
   // private final PaperToggleButton choiceTheme;


    public ChangeThemeComponent(Component headerComponent) {
        this.headerComponent = headerComponent;
        Icon moon = new Icon(VaadinIcon.MOON);
        Icon sun = new Icon(VaadinIcon.SUN_O);
     //   choiceTheme = new PaperToggleButton();
       // choiceTheme.addValueChangeListener(valueChangedEvent -> toggleTheme(valueChangedEvent.getValue()));
     //   add(sun, choiceTheme, moon);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setMargin(true);
    }

    private void setThemeFromCookie() {
        changeTheme(isLightThemeOn());
     //   choiceTheme.setCheckedProperty(!isLightThemeOn());
    }

    private void toggleTheme(boolean value) {
        changeTheme(!value);
        setLightThemeInCookie(!value);
    }

    public void changeTheme(boolean isLight) {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        if (isLight) {
            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            }
            themeList.add(Lumo.LIGHT);
        } else {
            if (themeList.contains(Lumo.LIGHT)) {
                themeList.remove(Lumo.LIGHT);
            }
            themeList.add(Lumo.DARK);
        }
        if (headerComponent!=null) {
            headerComponent.getElement().getThemeList().set("dark", !isLight);
        }
    }


    private void setLightThemeInCookie(boolean b) {
        Cookie myCookie = new Cookie(LIGHTMODE, b ? "true" : "false");
        // Make cookie expire in 2 minutes
        myCookie.setMaxAge(120);
        myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(myCookie);
    }

    private String getLightModeCookieValue() {
        for (Cookie c : VaadinService.getCurrentRequest().getCookies()) {
            if ("lightmode".equals(c.getName())) {
                String value = c.getValue();
                return value;
            }
        }
        return null;
    }

    private boolean isLightThemeOn() {
        String value = getLightModeCookieValue();
        if (value == null) {
            setLightThemeInCookie(true);
            return true;
        }
        return "true".equals(value);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        setThemeFromCookie();
        super.onAttach(attachEvent);
    }
}