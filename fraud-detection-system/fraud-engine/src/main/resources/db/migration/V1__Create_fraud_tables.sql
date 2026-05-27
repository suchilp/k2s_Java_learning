-- Fraud Engine Migration: Create Fraud Detection Tables

CREATE TABLE IF NOT EXISTS fraud_rules (
    id BIGSERIAL PRIMARY KEY,
    rule_name VARCHAR(255) NOT NULL,
    rule_description VARCHAR(1000),
    rule_type VARCHAR(50) NOT NULL,
    threshold DECIMAL(10,2),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fraud_alerts (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(100) NOT NULL,
    fraud_score DECIMAL(5,2) NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    rule_id BIGINT REFERENCES fraud_rules(id),
    status VARCHAR(50) DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fraud_patterns (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    pattern_type VARCHAR(100) NOT NULL,
    detected_value VARCHAR(500),
    detected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_fraud_alerts_tx_id ON fraud_alerts(transaction_id);
CREATE INDEX IF NOT EXISTS idx_fraud_alerts_status ON fraud_alerts(status);
CREATE INDEX IF NOT EXISTS idx_fraud_alerts_created ON fraud_alerts(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_fraud_patterns_user_id ON fraud_patterns(user_id);
