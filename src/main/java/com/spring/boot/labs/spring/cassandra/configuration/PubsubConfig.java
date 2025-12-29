package com.spring.boot.labs.spring.cassandra.configuration;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class PubsubConfig {

    @Value("${spring.cloud.gcp.pubsub.project-id}")
    String projectId;

    @Value("${spring.cloud.gcp.pubsub.topic}")
    String topic;


    private  final PubSubTemplate pubSubTemplate;


    public PubsubConfig(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }

    public void publishMessage(String message, Map<String, String> attributes) {
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFrom(message.getBytes(StandardCharsets.UTF_8)))
                .putAllAttributes(attributes)
                .build();

        pubSubTemplate.publish(TopicName.formatProjectTopicName(projectId, topic), pubsubMessage);
    }
}
