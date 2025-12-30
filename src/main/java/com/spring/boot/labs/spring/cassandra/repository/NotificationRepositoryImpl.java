package com.spring.boot.labs.spring.cassandra.repository;

import com.spring.boot.labs.spring.cassandra.model.NotificationEntity;
import com.spring.boot.labs.spring.cassandra.model.NotificationPrimaryKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationRepositoryImpl implements TTLRepository<NotificationEntity, NotificationPrimaryKey> {
    @Autowired
    CassandraOperations operations;

    public NotificationRepositoryImpl(CassandraOperations operations) {
        this.operations = operations;
    }


    @Override
    public NotificationEntity saveWithTtl(NotificationEntity notificationEntity, int ttl) {
        operations.insert(notificationEntity, InsertOptions.builder().ttl(ttl).build());
        return notificationEntity;
    }
}
