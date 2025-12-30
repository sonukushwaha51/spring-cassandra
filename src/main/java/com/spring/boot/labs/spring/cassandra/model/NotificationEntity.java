package com.spring.boot.labs.spring.cassandra.model;

import jakarta.persistence.GeneratedValue;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@Table("notification")
public class NotificationEntity {
    @PrimaryKey
    private NotificationPrimaryKey key;


    @Column("user_id")
    private String userId;

    @Column("notification_id")
    private String notificationId = UUID.randomUUID().toString();

    @Column("event_id")
    private String eventId;

    private Long timestamp;

    private String items;

}
