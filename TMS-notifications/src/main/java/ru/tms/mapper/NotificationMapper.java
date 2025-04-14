package ru.tms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tms.dto.Notification;
import ru.tms.entity.NotificationEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "userId", expression = "java(notificationEntity.getSubscription().getUserId())")
    Notification toDto(NotificationEntity notificationEntity);
    List<Notification> toDto(List<NotificationEntity> notificationEntities);
}