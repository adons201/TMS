package ru.tms.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import ru.tms.services.SubscriptionService;

public class SubscriptionButtonComponent extends Button {

    private final SubscriptionService subscriptionService;
    private final String targetType;
    private final Long targetId;
    private final String userId;
    private Boolean subscribeFlag = false;

    public SubscriptionButtonComponent(SubscriptionService subscriptionService, String targetType,
                                       Long targetId, String userId) {
        super();
        this.subscriptionService = subscriptionService;
        this.targetType = targetType;
        this.targetId = targetId;
        this.userId = userId;
        this.setTheme();
        this.addClickListener(event -> {
            if (!subscribeFlag) {
                subscriptionService.createSubscription(targetType, targetId, userId);
                this.getElement().getThemeList().set("contrast", false);
                this.getElement().getThemeList().set("success", true);
                this.setText("Unsubscribe");
                this.subscribeFlag = true;
            }
            else {
                subscriptionService.deleteSubscription(targetType, targetId, userId);
                this.getElement().getThemeList().set("success", false);
                this.getElement().getThemeList().set("contrast", true);
                this.setText("Subscribe");
                this.subscribeFlag = false;
            }
        });
    }

    private Boolean getStatusSubscription() {
        return subscriptionService.getSubscribeFlag(targetType, targetId, userId);
    }

    private void setTheme() {
        if (getStatusSubscription()) {
            this.getElement().getThemeList().add("badge success");
            this.setText("Unsubscribe");
            this.subscribeFlag = true;
        } else {
            this.getElement().getThemeList().add("badge contrast");
            this.setText("Subscribe");
            this.subscribeFlag = false;
        }
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs)");
        return icon;
    }
}
