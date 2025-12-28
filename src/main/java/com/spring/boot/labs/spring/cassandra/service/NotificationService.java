package com.spring.boot.labs.spring.cassandra.service;

import com.spring.boot.labs.spring.cassandra.model.NotificationEntity;
import com.spring.boot.labs.spring.cassandra.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public NotificationEntity save(NotificationEntity entity) {
        int expiration = Math.toIntExact(entity.getExpiration().getTime());
        return notificationRepository.saveWithTtl(entity, expiration);
    }

    public List<NotificationEntity> getAllNotificationsForUser(String userId, String eventId) {
        return notificationRepository.findAllByUserIdAndEventId(userId, eventId);
    }
}
