package ru.tms;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.LoadingIndicatorConfiguration;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import ru.tms.config.VaadinI18NProvider;

import static java.lang.System.setProperty;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@ComponentScan({"ru.tms.*"})
@Theme(value = "TMS-front")
@PWA(name = "TMS-front", shortName = "TMS")
@EnableDiscoveryClient
public class TmsFrontApplication extends SpringBootServletInitializer implements VaadinServiceInitListener,
        AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TmsFrontApplication.class, args);
    }

    @Override
    public void serviceInit(ServiceInitEvent initEvent) {

        setProperty("vaadin.i18n.provider", VaadinI18NProvider.class.getName());
        //LanguageSelect.readLanguageCookies(initEvent);

        initEvent.getSource().addUIInitListener(uiInitEvent -> {
            LoadingIndicatorConfiguration conf = uiInitEvent.getUI().getLoadingIndicatorConfiguration();

            // disable default theme -> loading indicator will not be shown
            conf.setApplyDefaultTheme(true);
        });

    }
}
