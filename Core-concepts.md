# Spring Cloud GCP Pub/Sub — Integration and Authentication Guide

This repository demonstrates a **production‑grade integration** of **Spring Boot** with **Google Cloud Pub/Sub** using **Spring Cloud GCP** and **Spring Integration**. It also documents the **correct authentication model**, including secure credential handling across local, GCP, and AWS environments.

The goal is to provide a clear reference for building **publishers and subscribers** without embedding credentials in code and while retaining full control over acknowledgements, retries, and scalability.

---

## Table of Contents

1. Overview
2. Architecture Overview
3. Core Spring Integration Components

    * ServiceActivator
    * MessageChannel
    * DirectChannel
    * PubSubInboundChannelAdapter
4. Publishing Messages
5. Consuming Messages

---

## 1. Overview

Spring Cloud GCP **does not provide annotation‑based listeners** for Pub/Sub (like `@KafkaListener`). Instead, it relies on **Spring Integration** for message consumption. This design gives explicit control over:

* Acknowledgement (`ack` / `nack`)
* Threading and concurrency
* Backpressure
* Retry and error handling

This repository follows that model intentionally.

---

## 2. Architecture Overview

```
Producer Service
   ↓
PubSubTemplate.publish()
   ↓
GCP Pub/Sub Topic
   ↓
PubSub Subscription
   ↓
PubSubInboundChannelAdapter
   ↓
MessageChannel
   ↓
@ServiceActivator
   ↓
Business Logic (DB, APIs, etc.)
```

---

## 3. Core Spring Integration Components

### 3.1 ServiceActivator

#### What it is

`@ServiceActivator` marks a method as a **message consumer endpoint**. This is where your application logic executes.

#### Library / Package

* Library: `spring-integration-core`
* Package: `org.springframework.integration.annotation.ServiceActivator`

#### Responsibilities

* Process message payloads
* Perform business logic
* Acknowledge (`ack`) or reject (`nack`) Pub/Sub messages

#### Example

```java
@Service
public class NotificationSubscriber {

    @ServiceActivator(inputChannel = "notificationInputChannel")
    public void handleMessage(
            Message<String> message,
            BasicAcknowledgeablePubsubMessage pubsubMessage) {

        try {
            String payload = message.getPayload();
            // business logic
            pubsubMessage.ack();
        } catch (Exception ex) {
            pubsubMessage.nack();
        }
    }
}
```

---

### 3.2 MessageChannel

#### What it is

`MessageChannel` is the **internal transport abstraction** used by Spring Integration to move messages between components.

#### Library / Package

* Library: `spring-integration-core`
* Package: `org.springframework.integration.channel`

#### Role

* Connects Pub/Sub adapters to application code
* Enables loose coupling and flexible flow design

#### Example

```java
@Bean
public MessageChannel notificationInputChannel() {
    return new DirectChannel();
}
```

---

### 3.3 DirectChannel

#### What it is

`DirectChannel` delivers messages **synchronously** on the caller’s thread.

#### Characteristics

* No queue
* One subscriber
* Low latency

#### When to use

* Short‑running logic
* Low to moderate throughput

#### When not to use

* Long‑running or blocking operations
* IO‑heavy workloads

For those cases, use `ExecutorChannel`.

---

### 3.4 PubSubInboundChannelAdapter

#### What it is

`PubSubInboundChannelAdapter` bridges a **Pub/Sub subscription** into Spring Integration.

#### Library / Package

* Library: `spring-cloud-gcp-pubsub`
* Package: `com.google.cloud.spring.pubsub.integration.inbound`

#### Responsibilities

* Pull messages from Pub/Sub
* Convert them into Spring `Message<?>`
* Forward them to a `MessageChannel`
* Control acknowledgement mode

#### Example

```java
@Bean
public PubSubInboundChannelAdapter notificationInboundAdapter(
        @Qualifier("notificationInputChannel") MessageChannel channel,
        PubSubTemplate pubSubTemplate) {

    PubSubInboundChannelAdapter adapter =
            new PubSubInboundChannelAdapter(pubSubTemplate, "notification-subscription");

    adapter.setOutputChannel(channel);
    adapter.setAckMode(AckMode.MANUAL);
    return adapter;
}
```

---

## 4. Publishing Messages

Publishing uses `PubSubTemplate`.

```java
@Service
public class NotificationPublisher {

    private final PubSubTemplate pubSubTemplate;

    public NotificationPublisher(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }

    public void publish(String topic, String payload) {
        pubSubTemplate.publish(topic, payload);
    }
}
```

The same credentials are used for publishing and subscribing.

---

## 5. Authentication and Credential Handling

### 5.1 Why Not Raw JSON Credentials

```java
GoogleCredentials.fromStream(
    new ByteArrayInputStream(jsonString.getBytes())
);
```

This approach is insecure because:

* Credentials can leak into source control
* Secrets may appear in logs or stack traces
* No IAM or audit control
* Manual rotation is required

---

### 5.2 Encrypted Credentials Explained

Encrypted credentials still contain the **same service account JSON**, but:

* Stored encrypted at rest
* Access controlled by IAM or secret managers
* Decrypted only at runtime
* Never embedded in code

Encryption protects **storage and access**, not the credential format.

---

### 5.3 Application Default Credentials (ADC)

Spring Cloud GCP automatically authenticates using ADC.

Credential sources:

* GCP metadata server (Cloud Run, GKE, GCE)
* `GOOGLE_APPLICATION_CREDENTIALS` environment variable
* Secure ADC file location

Typical configuration:

```yaml
spring:
  cloud:
    gcp:
      project-id: your-project-id
```

No authentication code is required.

---

## 6. Running in Different Environments

### Local Development

```bash
gcloud auth application-default login
```

### GCP (Cloud Run / GKE)

* Assign a service account
* Grant Pub/Sub IAM roles
* ADC uses the metadata server automatically

### AWS → GCP Pub/Sub

* ADC metadata is unavailable
* Provide a service account key via:

    * AWS Secrets Manager
    * Encrypted file or environment variable
* Set `GOOGLE_APPLICATION_CREDENTIALS`

The same credentials work for publishing and subscribing.

---