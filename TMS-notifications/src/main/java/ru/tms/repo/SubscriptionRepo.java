package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tms.entity.SubscriptionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepo extends JpaRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> getAllByTargetTypeAndTargetObjectId(String targetType, Long targetObjectId);
    Optional<SubscriptionEntity> getAllByTargetTypeAndTargetObjectIdAndUserId(String targetType, Long targetObjectId,
                                                                              String userId);
}
