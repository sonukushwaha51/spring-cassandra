package com.spring.boot.labs.spring.cassandra.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spring.boot.labs.spring.cassandra.model.NotificationDelete;
import com.spring.boot.labs.spring.cassandra.model.NotificationEntity;
import com.spring.boot.labs.spring.cassandra.repository.NotificationRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
@Slf4j
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PubsubMessageSender pubsubMessageSender;

    @Value("${notifications.ttl}")
    private int ttl;

    public NotificationEntity save(NotificationEntity entity) {
        ttl = ttl + LocalDateTime.now().getSecond();
        try {
            pubsubMessageSender.publishMessageToGcp(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return notificationRepository.saveWithTtl(entity, ttl);
    }

    public List<NotificationEntity> getAllNotificationsForUser(String userId, String eventId) {
        return notificationRepository.findAllByUserIdAndEventId(userId, eventId);
    }

    public void deleteNotificationByNotificationId(NotificationDelete notificationDelete) {
        log.info("Deleting notification");
        List<NotificationEntity> notificationEntities = notificationRepository.findAllByUserId(notificationDelete.getUserId());
        log.info("Notifications for userId {}, notifications: {}", notificationDelete.getUserId(), notificationEntities);
        notificationEntities.stream()
                .filter(notificationEntity -> notificationEntity.getNotificationId().equals(notificationDelete.getNotificationId()))
                .forEach(notificationEntity -> notificationRepository.delete(notificationEntity));
    }
}
