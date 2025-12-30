package com.spring.boot.labs.spring.cassandra.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
@Slf4j
public class PubsubConfig {

    @Value("${spring.cloud.gcp.pubsub.project-id}")
    String projectId;

    @Value("${spring.cloud.gcp.pubsub.topic}")
    String topic;


    private  final PubSubTemplate pubSubTemplate;

    GoogleCredentials googleCredentials;

    public PubsubConfig(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }

    public void publishMessage(String message, Map<String, String> attributes) {
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFromUtf8(message))
                .putAllAttributes(attributes)
                .build();
        try {
            if (googleCredentials == null) {
                init();
            }
            pubSubTemplate.publish(TopicName.formatProjectTopicName(projectId, topic), pubsubMessage);
        } catch (Exception exception) {
            log.error("Failed to get credentials from GCP", exception);
        }
    }

    public void init() throws IOException {
        GoogleCredentials.getApplicationDefault();
    }
}
