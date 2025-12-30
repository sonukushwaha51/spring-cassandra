package com.spring.boot.labs.spring.cassandra.model;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Data
@PrimaryKeyClass
public class NotificationPrimaryKey {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, name = "user_id")
    private String userId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, name = "event_id")
    private String eventId;

}
