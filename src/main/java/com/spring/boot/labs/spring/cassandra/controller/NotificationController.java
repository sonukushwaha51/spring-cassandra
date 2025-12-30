package com.spring.boot.labs.spring.cassandra.controller;

import com.spring.boot.labs.spring.cassandra.model.NotificationCreationResponse;
import com.spring.boot.labs.spring.cassandra.model.NotificationEntity;
import com.spring.boot.labs.spring.cassandra.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/notification")
    public ResponseEntity<NotificationCreationResponse> saveNotification(@RequestBody NotificationEntity notificationEntity) {
        NotificationEntity entity = notificationService.save(notificationEntity);
        NotificationCreationResponse response = new NotificationCreationResponse();
        response.setTimestamp(Instant.now().toEpochMilli());
        response.setEventId(entity.getEventId());
        response.setMessage("Notification saved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/list-notifications")
    public ResponseEntity<List<NotificationEntity>> getAllNotificationsForUser(@RequestParam(name = "userId", required = true) String userId,
                                                                               @RequestParam(name = "eventId", required = true) String eventId) {
        List<NotificationEntity> notificationEntities = notificationService.getAllNotificationsForUser(userId, eventId);
        return new ResponseEntity<>(notificationEntities, HttpStatus.OK);
    }
}
