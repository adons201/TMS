package ru.tms.mapper;

import org.mapstruct.Mapper;
import ru.tms.dto.Subscription;
import ru.tms.entity.SubscriptionEntity;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toDto(SubscriptionEntity subscriptionEntity);
    SubscriptionEntity toEntity(Subscription subscription);
}
