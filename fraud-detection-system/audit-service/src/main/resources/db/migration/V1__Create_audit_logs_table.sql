-- Flyway Migration: Create Audit Logs Table
-- Version: V1__Create_audit_logs_table.sql

CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    user_id VARCHAR(100),
    transaction_id VARCHAR(100),
    request_id VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    severity VARCHAR(20) NOT NULL DEFAULT 'INFO'
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_event_type ON audit_logs(event_type);
CREATE INDEX IF NOT EXISTS idx_created_at ON audit_logs(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_transaction_id ON audit_logs(transaction_id);
CREATE INDEX IF NOT EXISTS idx_user_id ON audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_severity ON audit_logs(severity);

-- Add comment to table
COMMENT ON TABLE audit_logs IS 'Stores all audit events for the fraud detection system';
COMMENT ON COLUMN audit_logs.event_type IS 'Type of event (e.g., LOGIN, TRANSACTION, FRAUD_DETECTED)';
COMMENT ON COLUMN audit_logs.severity IS 'Severity level: INFO, WARNING, ERROR, CRITICAL';
