package com.example.audit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventDTO {

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("severity")
    private String severity;
}
