package ru.tms.service;

import ru.tms.dto.Notification;
import ru.tms.entity.NotificationEntity;

import java.util.List;

public interface NotificationService {

    NotificationEntity getNotificationById(Long id);

    List<Notification> getAllNotificationsByUserId(String userId);

    Notification updateNotification(Notification notification, Long notificationId);

    List<Notification> createNotification(String targetType, Long targetObjectId, String content);

}
