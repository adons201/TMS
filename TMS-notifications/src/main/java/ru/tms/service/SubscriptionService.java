package ru.tms.service;

import ru.tms.dto.Subscription;
import ru.tms.entity.SubscriptionEntity;

import java.util.List;

public interface SubscriptionService {

    SubscriptionEntity getSubscriptionById(Long id);

    List<SubscriptionEntity> getSubscriptionsByTargetTypeAndTargetObjectId(String targetType, Long targetObjectId);

    Boolean getSubscribeFlag(String targetType, Long targetObjectId, String userId);

    Subscription createSubscription(String targetType, Long targetObjectId, String userId);

    void deleteSubscription(String targetType, Long targetObjectId, String userId);
}
