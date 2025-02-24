package ru.tms.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.PageTitle;

public class ReloadPage {

    public static void reloadPage(LocaleChangeEvent localeChangeEvent, Class<? extends Component> _class){
        if (localeChangeEvent.getUI().getInternals().getTitle() != null) {
            if (localeChangeEvent.getUI().getInternals().getTitle()
                    .equals(_class.getAnnotation(PageTitle.class).value())) {
                UI.getCurrent().getPage().reload();
            }
        }
    }

}
