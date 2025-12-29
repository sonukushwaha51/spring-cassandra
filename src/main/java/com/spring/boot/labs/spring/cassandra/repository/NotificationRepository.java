package com.spring.boot.labs.spring.cassandra.repository;

import com.spring.boot.labs.spring.cassandra.model.NotificationEntity;
import com.spring.boot.labs.spring.cassandra.model.NotificationPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface NotificationRepository extends TTLRepository<NotificationEntity, NotificationPrimaryKey>, CassandraRepository<NotificationEntity, NotificationPrimaryKey> {

    List<NotificationEntity> findAllByUserIdAndEventId(String userId, String eventId);

    List<NotificationEntity> findAllByUserId(String userId);
}
