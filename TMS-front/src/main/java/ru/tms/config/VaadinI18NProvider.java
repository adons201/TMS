package ru.tms.config;

import com.vaadin.flow.i18n.I18NProvider;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.ResourceBundle.getBundle;
import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.success;

public class VaadinI18NProvider implements I18NProvider {

    public static final String RESOURCE_BUNDLE_NAME = "vaadinapp";

    private static Locale ENGLISH = new Locale("EN");
    private static Locale RUSSIAN = new Locale("RU");

    private static final ResourceBundle RESOURCE_BUNDLE_EN = getBundle(RESOURCE_BUNDLE_NAME, ENGLISH);
    private static final ResourceBundle RESOURCE_BUNDLE_RU = getBundle(RESOURCE_BUNDLE_NAME, RUSSIAN);
    private static final List<Locale> providedLocales = unmodifiableList(asList(ENGLISH, RUSSIAN));

    @Override
    public List<Locale> getProvidedLocales() {
        return providedLocales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
//    logger().info("VaadinI18NProvider getTranslation.. key : " + key + " - " + locale);
        String result = "";
        result = match(
                matchCase(() -> success(RESOURCE_BUNDLE_EN)),
                matchCase(() -> RUSSIAN.equals(locale), () -> success(RESOURCE_BUNDLE_RU)),
                matchCase(() -> ENGLISH.equals(locale), () -> success(RESOURCE_BUNDLE_EN))
        )
                .map(resourceBundle -> {
                    if (!resourceBundle.containsKey(key))
                        System.out.println("missing ressource key (i18n) " + key);

                    return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : key;

                })
                .getOrElse(() -> key + " - " + locale);
        return result;

    }
}