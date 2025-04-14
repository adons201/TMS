package ru.tms.api;

import org.springframework.web.bind.annotation.*;
import ru.tms.dto.Notification;
import ru.tms.service.NotificationService;
import ru.tms.service.NotificationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/tms_notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/{userId}")
    public List<Notification> getNotifications(@PathVariable String userId) {
        return notificationService.getAllNotificationsByUserId(userId);
    }
}