package com.spring.boot.labs.spring.cassandra.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@Table(name = "notification")
public class NotificationEntity {
    @PrimaryKey
    private NotificationPrimaryKey key;


    @Column(name="user_id")
    private String userId;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id")
    private String notificationId;

    @Column(name = "event_id")
    private String eventId;

    private Long timestamp;

    private String items;

    private Date expiration = Date.from(Instant.now().plus(7, TimeUnit.MINUTES.toChronoUnit()));

}
