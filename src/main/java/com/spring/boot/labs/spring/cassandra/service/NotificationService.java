package com.spring.boot.labs.spring.cassandra.service;

import com.spring.boot.labs.spring.cassandra.model.NotificationDelete;
import com.spring.boot.labs.spring.cassandra.model.NotificationEntity;
import com.spring.boot.labs.spring.cassandra.repository.NotificationRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Value("${notifications.ttl}")
    private int ttl;

    public NotificationEntity save(NotificationEntity entity) {
        ttl = ttl + LocalDateTime.now().getSecond();
        return notificationRepository.saveWithTtl(entity, ttl);
    }

    public List<NotificationEntity> getAllNotificationsForUser(String userId, String eventId) {
        return notificationRepository.findAllByUserIdAndEventId(userId, eventId);
    }

    public void deleteNotificationByNotificationId(NotificationDelete notificationDelete) {
        List<NotificationEntity> notificationEntities = notificationRepository.findAllByUserId(notificationDelete.getUserId());
        notificationEntities.stream()
                .filter(notificationEntity -> notificationEntity.getNotificationId().equals(notificationDelete.getNotificationId()))
                .forEach(notificationEntity -> notificationRepository.delete(notificationEntity));
    }
}
