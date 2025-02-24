package ru.tms.components;

//import ch.carnet.kasparscherrer.LanguageSelect;
import com.vaadin.flow.component.dependency.CssImport;
import lombok.Getter;

import java.util.Locale;

@CssImport("./styles/shared-styles.css")
public class LanguageSelectView /*extends LanguageSelect*/ {

//    @Getter
//    private LanguageSelect langSelect;

    public LanguageSelectView() {
        boolean useLanguageCookies = true;
//        langSelect = new LanguageSelect(useLanguageCookies, new Locale("ru"), new Locale("en"));
//        langSelect.getElement().getStyle().set("--lumo-contrast-10pct", "none");
//        langSelect.getElement().getStyle().set("--lumo-body-text-color", "none");
//        add(langSelect);
    }
}
