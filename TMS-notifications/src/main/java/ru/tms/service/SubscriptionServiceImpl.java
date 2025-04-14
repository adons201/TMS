package ru.tms.service;

import org.springframework.stereotype.Service;
import ru.tms.dto.Subscription;
import ru.tms.entity.SubscriptionEntity;
import ru.tms.mapper.SubscriptionMapper;
import ru.tms.repo.SubscriptionRepo;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepo subscriptionRepo;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionServiceImpl(SubscriptionRepo subscriptionRepo, SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepo = subscriptionRepo;
        this.subscriptionMapper = subscriptionMapper;
    }

    public SubscriptionEntity getSubscriptionById(Long id) {
        return this.subscriptionRepo.findById(id).get();
    }

    public List<SubscriptionEntity> getSubscriptionsByTargetTypeAndTargetObjectId(String targetType,
                                                                                  Long targetObjectId) {
        return this.subscriptionRepo.getAllByTargetTypeAndTargetObjectId(targetType, targetObjectId);
    }

    public SubscriptionEntity getSubscriptionsByTargetTypeAndTargetObjectIdAndUserId(String targetType,
                                                                                     Long targetObjectId,
                                                                                     String userId) {
        return this.subscriptionRepo
                .getAllByTargetTypeAndTargetObjectIdAndUserId(targetType, targetObjectId, userId).get();
    }

    public Boolean getSubscribeFlag(String targetType, Long targetObjectId, String userId) {
        return this.subscriptionRepo
                .getAllByTargetTypeAndTargetObjectIdAndUserId(targetType, targetObjectId, userId).isPresent();
    }

    public Subscription createSubscription(String targetType, Long targetObjectId, String userId) {
        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .targetType(targetType)
                .targetObjectId(targetObjectId)
                .userId(userId)
                .build();
        return this.subscriptionMapper.toDto(this.subscriptionRepo.save(subscription));
    }

    public void deleteSubscription(String targetType, Long targetObjectId, String userId) {
        SubscriptionEntity subscription = this.getSubscriptionsByTargetTypeAndTargetObjectIdAndUserId(targetType,
                targetObjectId, userId);
        subscriptionRepo.deleteById(subscription.getId());
    }
}
