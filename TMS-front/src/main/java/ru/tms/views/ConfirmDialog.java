package ru.tms.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/shared-styles.css")
public class ConfirmDialog extends Dialog {

    public ConfirmDialog(String caption, String text, String confirmButtonText,
                         Runnable confirmListener) {

        final VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        add(content);

        add(new H3(caption));
        add(new Span(text));

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setPadding(false);
        add(buttons);

        final Button confirm = new Button(confirmButtonText, e -> {
            confirmListener.run();
            close();
        });
        confirm.setId("del");
        final Button cancel = new Button(getTranslation("cancel"), e -> close());
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttons.add(cancel);
        buttons.add(confirm);
    }
}
