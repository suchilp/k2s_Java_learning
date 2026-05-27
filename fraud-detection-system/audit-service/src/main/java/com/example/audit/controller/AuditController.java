package com.example.audit.controller;

import com.example.audit.dto.AuditEventDTO;
import com.example.audit.model.AuditLog;
import com.example.audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/audit")
@Slf4j
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Log an audit event
     */
    @PostMapping("/log")
    public ResponseEntity<Map<String, Object>> logEvent(@RequestBody AuditEventDTO event) {
        try {
            AuditLog savedLog = auditService.logEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "SUCCESS",
                    "message", "Audit event logged successfully",
                    "id", savedLog.getId()
            ));
        } catch (Exception e) {
            log.error("Error logging audit event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "ERROR",
                    "message", "Failed to log audit event: " + e.getMessage()
            ));
        }
    }

    /**
     * Get logs by event type
     */
    @GetMapping("/logs/event-type/{eventType}")
    public ResponseEntity<List<AuditLog>> getLogsByEventType(@PathVariable String eventType) {
        List<AuditLog> logs = auditService.getLogsByEventType(eventType);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get logs by transaction ID
     */
    @GetMapping("/logs/transaction/{transactionId}")
    public ResponseEntity<List<AuditLog>> getLogsByTransactionId(@PathVariable String transactionId) {
        List<AuditLog> logs = auditService.getLogsByTransactionId(transactionId);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get logs by user ID
     */
    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<AuditLog>> getLogsByUserId(@PathVariable String userId) {
        List<AuditLog> logs = auditService.getLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get logs by date range
     */
    @GetMapping("/logs/date-range")
    public ResponseEntity<List<AuditLog>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLog> logs = auditService.getLogsByDateRange(startDate, endDate);
        return ResponseEntity.ok(logs);
    }

    /**
     * Get logs by severity
     */
    @GetMapping("/logs/severity/{severity}")
    public ResponseEntity<List<AuditLog>> getLogsBySeverity(@PathVariable String severity) {
        List<AuditLog> logs = auditService.getLogsBySeverity(severity);
        return ResponseEntity.ok(logs);
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "audit-service"));
    }
}

