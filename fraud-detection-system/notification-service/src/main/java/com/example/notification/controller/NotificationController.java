package com.example.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody NotificationRequest request) {
        return ResponseEntity.ok("NOTIFICATION_SENT");
    }

    static class NotificationRequest {
        public String userId;
        public String message;
    }
}
