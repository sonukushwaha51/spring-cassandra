package com.spring.boot.labs.spring.cassandra.model;

import lombok.Data;

@Data
public class NotificationDelete {

    private String userId;

    private String notificationId;
}
