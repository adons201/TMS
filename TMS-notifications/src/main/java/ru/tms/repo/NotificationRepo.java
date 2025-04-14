package ru.tms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tms.entity.NotificationEntity;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationEntity, Long> {

    @Query("select notification " +
            " from NotificationEntity notification " +
            "where notification.subscription.userId = :userId" +
            "  and notification.read = :read")
    List<NotificationEntity> getAllByUserIdAndRead(String userId, Boolean read);
}
