package ru.tms.services.client;

import ru.tms.dto.Notification;

import java.util.List;

public interface NotificationClient {

    List<Notification> getNotifications(String userId);
}
