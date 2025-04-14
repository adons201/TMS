package ru.tms.services.client;

import org.springframework.http.ResponseEntity;
import ru.tms.dto.Subscription;

public interface SubscriptionClient {

    Boolean getSubscribeFlag(String targetType, Long targetObjectId, String userId);

    Subscription createSubscription(String targetType, Long targetObjectId, String userId);

    ResponseEntity<Void> deleteSubscription(String targetType, Long targetObjectId, String userId);
}