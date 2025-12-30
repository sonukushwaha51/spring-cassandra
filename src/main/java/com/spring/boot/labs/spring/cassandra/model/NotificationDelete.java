package com.spring.boot.labs.spring.cassandra.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDelete {

    private String userId;

    private String notificationId;
}
