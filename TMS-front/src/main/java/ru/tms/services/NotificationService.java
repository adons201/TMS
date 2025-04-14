package ru.tms.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import ru.tms.dto.Notification;
import ru.tms.services.client.NotificationClient;

import java.util.List;

@RequiredArgsConstructor
public class NotificationService implements NotificationClient {

    private final RestClient restClient;

    @Override
    public List<Notification> getNotifications(String userId) {
        return this.restClient.get()
                .uri("/tms_notification/notifications/{userId}", userId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Notification>>() {
                });
    }
}