package com.spring.boot.labs.spring.cassandra.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.boot.labs.spring.cassandra.configuration.PubsubConfig;
import com.spring.boot.labs.spring.cassandra.model.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PubsubMessageSender {

    @Autowired
    PubsubConfig pubsubConfig;

    @Autowired
    ObjectMapper objectMapper;

    public void publishMessageToGcp(NotificationEntity notificationEntity) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(notificationEntity);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("id", UUID.randomUUID().toString());
        pubsubConfig.publishMessage(message, attributes);
    }

}
