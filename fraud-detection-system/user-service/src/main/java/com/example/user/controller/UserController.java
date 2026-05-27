package com.example.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> profile() {
        UserProfile profile = new UserProfile();
        profile.username = "demo_user";
        profile.email = "demo@example.com";
        profile.mobile = "+1234567890";
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/device/register")
    public ResponseEntity<String> registerDevice(@RequestBody DeviceRequest request) {
        return ResponseEntity.ok("DEVICE_REGISTERED");
    }

    static class UserProfile {
        public String username;
        public String email;
        public String mobile;
    }

    static class DeviceRequest {
        public String deviceId;
        public String ipAddress;
        public String location;
    }
}
