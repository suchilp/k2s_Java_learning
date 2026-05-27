package com.example.audit.service;

import com.example.audit.dto.AuditEventDTO;
import com.example.audit.model.AuditLog;
import com.example.audit.repository.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Log an audit event
     */
    public AuditLog logEvent(AuditEventDTO event) {
        AuditLog.AuditSeverity severity;
        try {
            severity = AuditLog.AuditSeverity.valueOf(event.getSeverity().toUpperCase());
        } catch (Exception e) {
            severity = AuditLog.AuditSeverity.INFO;
        }

        AuditLog auditLog = AuditLog.builder()
                .eventType(event.getEventType())
                .description(event.getDescription())
                .userId(event.getUserId())
                .transactionId(event.getTransactionId())
                .requestId(event.getRequestId())
                .severity(severity)
                .build();

        AuditLog saved = auditLogRepository.save(auditLog);
        log.info("Audit event logged: {} - RequestID: {}", event.getEventType(), event.getRequestId());
        return saved;
    }

    /**
     * Retrieve audit logs by event type
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByEventType(String eventType) {
        return auditLogRepository.findByEventType(eventType);
    }

    /**
     * Retrieve audit logs by transaction ID
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByTransactionId(String transactionId) {
        return auditLogRepository.findByTransactionId(transactionId);
    }

    /**
     * Retrieve audit logs by user ID
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUserId(String userId) {
        return auditLogRepository.findByUserId(userId);
    }

    /**
     * Retrieve audit logs by date range
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Retrieve audit logs by severity
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsBySeverity(String severity) {
        try {
            AuditLog.AuditSeverity auditSeverity = AuditLog.AuditSeverity.valueOf(severity.toUpperCase());
            return auditLogRepository.findBySeverity(auditSeverity);
        } catch (Exception e) {
            log.warn("Invalid severity level: {}", severity);
            return List.of();
        }
    }
}
