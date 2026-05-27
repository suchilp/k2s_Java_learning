package com.example.fraud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fraud")
public class FraudController {

    @PostMapping("/evaluate")
    public ResponseEntity<String> evaluate(@RequestBody RiskRequest request) {
        int score = 0;
        if (request.largeAmount) score += 40;
        if (request.newDevice) score += 30;
        if (request.foreignLocation) score += 50;

        if (score <= 40) {
            return ResponseEntity.ok("APPROVED");
        } else if (score <= 70) {
            return ResponseEntity.ok("OTP_VERIFICATION");
        }
        return ResponseEntity.ok("BLOCKED");
    }

    static class RiskRequest {
        public boolean newDevice;
        public boolean largeAmount;
        public boolean foreignLocation;
    }
}
