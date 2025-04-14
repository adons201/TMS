package ru.tms.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.tms.dto.Subscription;
import ru.tms.services.client.SubscriptionClient;

@RequiredArgsConstructor
public class SubscriptionService implements SubscriptionClient {

    private final RestClient restClient;

    @Override
    public Boolean getSubscribeFlag(String targetType, Long targetObjectId, String userId) {
        return this.restClient.get()
                .uri(UriComponentsBuilder.fromPath("/subscription")
                        .queryParam("targetType", targetType)
                        .queryParam("targetObjectId", targetObjectId)
                        .queryParam("userId", userId)
                        .build().toUriString())
                .retrieve()
                .body(Boolean.class);
    }

    @Override
    public Subscription createSubscription(String targetType, Long targetObjectId, String userId) {
        return this.restClient.post()
                .uri(UriComponentsBuilder.fromPath("/subscription")
                        .queryParam("targetType", targetType)
                        .queryParam("targetObjectId", targetObjectId)
                        .queryParam("userId", userId)
                        .build().toUriString())
                .retrieve()
                .body(Subscription.class);
    }

    @Override
    public ResponseEntity<Void> deleteSubscription(String targetType, Long targetObjectId, String userId) {
        return this.restClient.delete()
                .uri(UriComponentsBuilder.fromPath("/subscription")
                        .queryParam("targetType", targetType)
                        .queryParam("targetObjectId", targetObjectId)
                        .queryParam("userId", userId)
                        .build().toUriString())
                .retrieve()
                .toBodilessEntity();
    }
}