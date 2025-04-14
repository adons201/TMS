package ru.tms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tms.dto.Notification;
import ru.tms.entity.NotificationEntity;
import ru.tms.entity.SubscriptionEntity;
import ru.tms.mapper.NotificationMapper;
import ru.tms.repo.NotificationRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final NotificationMapper notificationMapper;
    private final SubscriptionServiceImpl subscriptionService;

    public NotificationServiceImpl(NotificationRepo notificationRepo, NotificationMapper notificationMapper,
                                   SubscriptionServiceImpl subscriptionService) {
        this.notificationRepo = notificationRepo;
        this.notificationMapper = notificationMapper;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public NotificationEntity getNotificationById(Long id) {
        return this.notificationRepo.findById(id).get();
    }

    @Override
    public List<Notification> getAllNotificationsByUserId(String userId) {
        return this.notificationMapper.toDto(this.notificationRepo.getAllByUserIdAndRead(userId, false));
    }

    @Override
    @Transactional
    public Notification updateNotification(Notification notification, Long notificationId) {
        NotificationEntity notificationEntity = this.getNotificationById(notificationId);
        notificationEntity.setRead(true);
        return this.notificationMapper.toDto(this.notificationRepo.save(notificationEntity));
    }

    @Override
    @Transactional
    public List<Notification> createNotification(String targetType, Long targetObjectId, String content) {
        List<Notification> listNotification = new ArrayList<>();
        for (SubscriptionEntity subscriptionEntity : this.subscriptionService.
                getSubscriptionsByTargetTypeAndTargetObjectId(targetType, targetObjectId)) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setSubscription(subscriptionEntity);
            notificationEntity.setContent(content);
            notificationEntity.setRead(false);
            listNotification.add(this.notificationMapper.toDto(this.notificationRepo.save(notificationEntity)));
        }
        return listNotification;
    }
}