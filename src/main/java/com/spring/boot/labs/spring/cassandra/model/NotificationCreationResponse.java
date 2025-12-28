package com.spring.boot.labs.spring.cassandra.model;

import lombok.Data;

import java.time.Instant;

@Data
public class NotificationCreationResponse {

    private Long timestamp = Instant.now().toEpochMilli();

    private String eventId;

    private String message;
}
