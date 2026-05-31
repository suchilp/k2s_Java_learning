# System Design Document
## Online Banking Fraud Detection System

**Version:** 1.0  
**Date:** May 30, 2026  
**Project:** Fraud Detection System  
**Author:** Development Team

---

## 1. Executive Summary

This document describes the architecture, design, and flow of the Online Banking Fraud Detection System. The system is designed to detect suspicious banking transactions in real-time using rule-based detection and machine learning models.

**Key Objectives:**
- Real-time transaction monitoring
- Risk-based authentication (OTP/Block)
- Machine learning fraud prediction
- Comprehensive audit trail
- High availability & resilience

---

## 2. System Architecture

### 2.1 High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                      CLIENT TIER                                 │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │         React Frontend (Port 3000)                       │   │
│  │  - Login Page, Dashboard, Transaction Submission        │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              ↓ HTTPS
┌─────────────────────────────────────────────────────────────────┐
│                     API TIER                                     │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  API Gateway (Port 8080)                                │   │
│  │  - Request routing, Circuit breaker, Authentication     │   │
│  │  - Load balancing, Rate limiting                        │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                    ↓ (6 Route Paths)
┌──────────────┬──────────────┬──────────────┬──────────────┬──────────────┬──────────────┐
│              │              │              │              │              │              │
v              v              v              v              v              v              v
┌──────────────────────────────────────────────────────────────────────────────────────┐
│                         MICROSERVICES TIER                                           │
│                                                                                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │Auth Service  │  │Transaction   │  │ Fraud Engine │  │ User Service │            │
│  │  (8081)      │  │ Service(8082)│  │   (8083)     │  │   (8085)     │            │
│  │              │  │              │  │              │  │              │            │
│  │- JWT Token   │  │- Receive Tx  │  │- Risk Score  │  │- Profile     │            │
│  │- OTP Verify  │  │- Kafka Pub   │  │- Rule Engine │  │- Devices     │            │
│  │- Register    │  │- Validate    │  │- Decision    │  │- Location    │            │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘            │
│                                                                                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                              │
│  │Notification  │  │Audit Service │  │ML Model      │                              │
│  │Service(8084) │  │  (8086)      │  │Service       │                              │
│  │              │  │              │  │(Python)      │                              │
│  │- Email/SMS   │  │- Log Events  │  │- Predict     │                              │
│  │- Alerts      │  │- Query Logs  │  │- Model       │                              │
│  │- OTP         │  │- Audit Trail │  │- Serialize   │                              │
│  └──────────────┘  └──────────────┘  └──────────────┘                              │
│                                                                                      │
└──────────────────────────────────────────────────────────────────────────────────────┘
         ↓ Kafka Event Stream          ↓ SQL Queries        ↓ API Calls
┌──────────────────────────────────────────────────────────────────┐
│                      INFRASTRUCTURE TIER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │  Kafka       │  │  PostgreSQL  │  │   Redis      │            │
│  │ (Streaming)  │  │  (Database)  │  │  (Cache)     │            │
│  └──────────────┘  └──────────────┘  └──────────────┘            │
└──────────────────────────────────────────────────────────────────┘
```

---

## 3. Component Description

### 3.1 Frontend (React)

**Port:** 3000  
**Technology:** React 18, Redux Toolkit, Material-UI

**Key Components:**
- `App.js` - Main routing & layout
- `LoginPage.js` - Authentication UI
- `DashboardPage.js` - Transaction submission
- `PrivateRoute.js` - Protected routes
- `authSlice.js` - Redux auth state

**Features:**
- User authentication with JWT
- Transaction submission form
- Real-time fraud result display
- Material Design UI

---

### 3.2 API Gateway

**Port:** 8080  
**Technology:** Spring Cloud Gateway, Resilience4j

**Routes:**
```
/auth/**       → Auth Service (8081)
/transaction/** → Transaction Service (8082)
/fraud/**      → Fraud Engine (8083)
/user/**       → User Service (8085)
/notification/**→ Notification Service (8084)
/audit/**      → Audit Service (8086)
```

**Features:**
- Circuit breaker per service
- Fallback mechanism
- Request ID tracking
- Rate limiting ready

---

### 3.3 Auth Service

**Port:** 8081  
**Technology:** Spring Boot, JWT, JPA

**Endpoints:**
```
POST /auth/register      - Register new user
POST /auth/login         - Authenticate user
POST /auth/verify-otp    - Verify OTP
POST /auth/refresh-token - Refresh JWT
```

**Database:**
- `users` - User accounts
- `sessions` - Active sessions

---

### 3.4 Transaction Service

**Port:** 8082  
**Technology:** Spring Boot, Kafka Producer

**Endpoints:**
```
POST /transaction/transfer - Submit transaction
```

**Data Flow:**
1. Receives transaction from API Gateway
2. Validates transaction
3. Publishes to `transaction-events` Kafka topic
4. Returns acknowledgment

**Request Model:**
```json
{
  "sender": "user_id",
  "receiver": "recipient_id", 
  "amount": 1000.50
}
```

---

### 3.5 Fraud Engine

**Port:** 8083  
**Technology:** Spring Boot, Rule Engine

**Endpoints:**
```
POST /fraud/evaluate - Evaluate transaction risk
```

**Risk Scoring Algorithm:**
```
Base Score = 0

if (amount > threshold)        → score += 40
if (new_device_detected)       → score += 30
if (foreign_location)          → score += 50
if (unusual_time)              → score += 20

Decision Logic:
  if (score <= 40)   → APPROVED
  if (score <= 70)   → OTP_VERIFICATION
  if (score > 70)    → BLOCKED
```

**Database:**
- `fraud_rules` - Detection rules
- `fraud_alerts` - Alert records
- `fraud_patterns` - Pattern history

---

### 3.6 User Service

**Port:** 8085  
**Technology:** Spring Boot

**Endpoints:**
```
GET  /user/profile          - Get user profile
POST /user/device/register  - Register device
```

**Features:**
- Device fingerprinting
- IP tracking
- Location-based detection
- Behavioral analysis ready

---

### 3.7 Notification Service

**Port:** 8084  
**Technology:** Spring Boot, Kafka Consumer

**Features:**
- Email notifications
- SMS alerts
- OTP delivery
- Fraud alerts

**Kafka Consumer:**
- Listens on `notification-events` topic
- Processes alerts asynchronously

---

### 3.8 Audit Service

**Port:** 8086  
**Technology:** Spring Boot, JPA

**Endpoints:**
```
POST   /audit/log                        - Log event
GET    /audit/logs/event-type/{type}    - Filter by type
GET    /audit/logs/transaction/{id}     - Filter by TX
GET    /audit/logs/user/{id}            - Filter by user
GET    /audit/logs/date-range           - Date filter
GET    /audit/logs/severity/{severity}  - Filter by severity
GET    /audit/health                    - Health check
```

**Database:**
- `audit_logs` - All system events

**Event Types:**
- LOGIN, LOGOUT
- TRANSACTION_CREATED
- FRAUD_DETECTED
- OTP_SENT
- BLOCK_ATTEMPTED

---

### 3.9 ML Model Service (Python)

**Technology:** FastAPI, Scikit-learn

**Endpoint:**
```
POST /predict - Predict fraud
```

**Model:** Isolation Forest
- Input: Transaction features
- Output: Fraud prediction (0/1)
- Serialization: joblib (`fraud_model.pkl`)

**Training:**
- 200 synthetic samples
- 5 features per transaction
- Persistence layer for model reuse

---

## 4. Data Flow Diagrams

### 4.1 User Authentication Flow

```
┌─────────┐
│  User   │
└────┬────┘
     │ 1. Enter credentials
     v
┌─────────────────────────────────┐
│  React Frontend (LoginPage)     │
│  username + password            │
└────┬────────────────────────────┘
     │ 2. POST /auth/login
     v
┌─────────────────────────────────┐
│  API Gateway (8080)             │
│  Route to Auth Service          │
└────┬────────────────────────────┘
     │ 3. Route: /auth/**
     v
┌─────────────────────────────────┐
│  Auth Service (8081)            │
│  - Validate credentials         │
│  - Hash password check          │
│  - Generate JWT token          │
└────┬────────────────────────────┘
     │ 4. Return: { token, user }
     v
┌─────────────────────────────────┐
│  React Redux Store              │
│  - Save token (localStorage)    │
│  - Save user profile            │
│  - Set isAuthenticated = true   │
└────┬────────────────────────────┘
     │ 5. Redirect to /dashboard
     v
┌─────────────────────────────────┐
│  Dashboard Page                 │
│  (Protected Route)              │
└─────────────────────────────────┘
```

---

### 4.2 Transaction Submission & Fraud Detection Flow

```
┌──────────────────────────────────────────────────────────────────────┐
│                    USER ACTION                                       │
│  Dashboard → Submit Transaction Form                                 │
└────┬─────────────────────────────────────────────────────────────────┘
     │ Amount, Merchant, Description
     │ Authorization: Bearer {JWT_TOKEN}
     v
┌──────────────────────────────────────────────────────────────────────┐
│                    STEP 1: TRANSACTION SUBMISSION                    │
│  Frontend → POST /transaction/transfer → API Gateway (8080)          │
└────┬─────────────────────────────────────────────────────────────────┘
     │ Route: /transaction/**
     v
┌──────────────────────────────────────────────────────────────────────┐
│                    STEP 2: TRANSACTION SERVICE (8082)                │
│  - Receive transaction                                               │
│  - Validate amount & merchant                                        │
│  - Create transaction ID                                             │
│  - Publish to Kafka: "transaction-events"                            │
└────┬─────────────────────────────────────────────────────────────────┘
     │ Kafka Event
     v
┌──────────────────────────────────────────────────────────────────────┐
│                    STEP 3: FRAUD ENGINE CONSUMER (8083)              │
│  - Receive event from Kafka                                          │
│  - Extract transaction details                                       │
│  - Call /fraud/evaluate                                              │
└────┬─────────────────────────────────────────────────────────────────┘
     │
     v
┌──────────────────────────────────────────────────────────────────────┐
│                    STEP 4: FRAUD SCORING                             │
│                                                                      │
│  Risk Score Calculation:                                             │
│  ┌──────────────────────────────────────────┐                        │
│  │ score = 0                                │                        │
│  │ if (amount > $5000) score += 40          │                        │
│  │ if (new_device) score += 30              │                        │
│  │ if (foreign_country) score += 50         │                        │
│  └──────────────────────────────────────────┘                        │
│                                                                      │
│  Decision Matrix:                                                    │
│  ┌────────────────────────────────┐                                  │
│  │ if score ≤ 40  → APPROVED      │                                  │
│  │ if score ≤ 70  → OTP_REQUIRED  │                                  │
│  │ if score > 70  → BLOCKED       │                                  │
│  └────────────────────────────────┘                                  │
└────┬─────────────────────────────────────────────────────────────────┘
     │ Decision Result
     v
┌──────────────────────────────────────────────────────────────────────┐
│                    STEP 5: RESPONSE ROUTING                          │
│                                                                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────────┐    │
│  │ APPROVED        │  │ OTP_REQUIRED    │  │ BLOCKED          │    │
│  │                 │  │                 │  │                  │    │
│  │ Return to       │  │ Trigger OTP     │  │ Block TX         │    │
│  │ Dashboard:      │  │ Send via SMS    │  │ Notify user      │    │
│  │ Status = OK     │  │ Return to       │  │ Log incident     │    │
│  │                 │  │ Dashboard       │  │                  │    │
│  │                 │  │ User enters OTP │  │                  │    │
│  └─────────────────┘  └─────────────────┘  └──────────────────┘    │
└────┬─────────────────────────────────────────────────────────────────┘
     │
     v
┌──────────────────────────────────────────────────────────────────────┐
│                    STEP 6: AUDIT LOGGING                             │
│  - Audit Service logs all events                                     │
│  - Event type: TRANSACTION_SUBMITTED, FRAUD_EVALUATED, OTP_SENT      │
│  - Severity: INFO / WARNING / ERROR                                  │
│  - Queryable by: event, user, transaction, date, severity            │
└──────────────────────────────────────────────────────────────────────┘
     │
     v
┌──────────────────────────────────────────────────────────────────────┐
│                    STEP 7: NOTIFICATION                              │
│  - Notification Service sends alert                                  │
│  - Email: Transaction details                                        │
│  - SMS: OTP (if required)                                            │
│  - Push: Fraud alert (if blocked)                                    │
└──────────────────────────────────────────────────────────────────────┘
```

---

### 4.3 ML Model Prediction Flow

```
┌─────────────────────────────────────┐
│  Fraud Engine (Java Service)        │
│  Has transaction data               │
└────┬────────────────────────────────┘
     │ 1. Extract features:
     │    - amount
     │    - merchant_category
     │    - time_of_day
     │    - user_velocity
     │    - device_new_flag
     v
┌─────────────────────────────────────┐
│  ML Model Service (Python)          │
│  FastAPI on dedicated port          │
└────┬────────────────────────────────┘
     │ 2. HTTP POST /predict
     │    body: [features...]
     v
┌─────────────────────────────────────┐
│  Model Processing                   │
│  - Load fraud_model.pkl             │
│  - Isolation Forest algorithm       │
│  - Input: feature vector            │
│  - Output: anomaly score            │
└────┬────────────────────────────────┘
     │ 3. Return prediction
     │    { "prediction": 0 or 1 }
     │    0 = normal
     │    1 = fraud
     v
┌─────────────────────────────────────┐
│  Fraud Engine                       │
│  - Combine with rule-based score    │
│  - Generate final decision          │
│  - Store result in DB               │
└─────────────────────────────────────┘
```

---

### 4.4 Audit Logging Flow

```
┌─────────────────────────────────────────────┐
│  Any Service (Auth, Fraud, Notification)   │
└────┬────────────────────────────────────────┘
     │ 1. Generate audit event
     │    {
     │      event_type: "FRAUD_DETECTED",
     │      user_id: "user_123",
     │      transaction_id: "tx_456",
     │      severity: "WARNING",
     │      description: "High risk score: 85"
     │    }
     v
┌─────────────────────────────────────────────┐
│  Audit Service (8086)                      │
│  POST /audit/log                           │
└────┬────────────────────────────────────────┘
     │ 2. Store in PostgreSQL
     │    audit_logs table
     │    - Index by event_type
     │    - Index by user_id
     │    - Index by transaction_id
     │    - Index by created_at
     │    - Index by severity
     v
┌─────────────────────────────────────────────┐
│  Database Query Capabilities               │
│  GET /audit/logs/event-type/FRAUD_DETECTED  │
│  GET /audit/logs/user/user_123              │
│  GET /audit/logs/severity/ERROR             │
│  GET /audit/logs/date-range?from=...&to=...│
└─────────────────────────────────────────────┘
```

---

## 5. Database Schema

### 5.1 Auth Service Database

```sql
-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sessions Table
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    token VARCHAR(500) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 5.2 Fraud Engine Database

```sql
-- Fraud Rules
CREATE TABLE fraud_rules (
    id BIGSERIAL PRIMARY KEY,
    rule_name VARCHAR(255) NOT NULL,
    rule_type VARCHAR(50) NOT NULL,
    threshold DECIMAL(10,2),
    is_active BOOLEAN DEFAULT true
);

-- Fraud Alerts
CREATE TABLE fraud_alerts (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(100) NOT NULL,
    fraud_score DECIMAL(5,2) NOT NULL,
    risk_level VARCHAR(20),
    status VARCHAR(50) DEFAULT 'OPEN'
);

-- Fraud Patterns
CREATE TABLE fraud_patterns (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100),
    pattern_type VARCHAR(100),
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 5.3 Audit Service Database

```sql
-- Audit Logs
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    user_id VARCHAR(100),
    transaction_id VARCHAR(100),
    severity VARCHAR(20) DEFAULT 'INFO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_event_type ON audit_logs(event_type);
CREATE INDEX idx_user_id ON audit_logs(user_id);
CREATE INDEX idx_transaction_id ON audit_logs(transaction_id);
CREATE INDEX idx_severity ON audit_logs(severity);
CREATE INDEX idx_created_at ON audit_logs(created_at DESC);
```

---

## 6. Security Architecture

### 6.1 Authentication & Authorization

```
Step 1: User Credentials
├─ Username/Password submitted via HTTPS
├─ Frontend → API Gateway → Auth Service
└─ Password hashed with BCrypt

Step 2: JWT Token Generation
├─ Auth Service generates JWT token
├─ Token payload: user_id, username, exp
└─ Signed with private key

Step 3: Token Usage
├─ Frontend stores token in localStorage
├─ Attach to all API calls: Authorization: Bearer {token}
├─ API Gateway validates token signature
└─ Microservices verify token claims

Step 4: OTP for High-Risk Transactions
├─ Fraud Engine triggers OTP for 41-70 score
├─ Notification Service sends OTP via SMS
├─ User enters OTP in frontend
└─ Verified in Auth Service before transaction approval
```

### 6.2 Circuit Breaker Pattern

```
API Gateway Routes
├─ Health Check: Monitors service availability
├─ States:
│  ├─ CLOSED: Normal operation (passing through)
│  ├─ OPEN: Service down (return fallback)
│  └─ HALF_OPEN: Testing recovery
├─ Threshold: 50% failure rate
├─ Fallback: Return service unavailable response
└─ Timeout: 10 seconds
```

---

## 7. Integration Points

### 7.1 Event Streaming (Kafka)

**Topics:**
- `transaction-events` - Transaction submitted
- `fraud-alerts` - Fraud detected
- `notification-events` - Send notification
- `audit-logs` - Log event

**Publisher-Subscriber:**
```
Transaction Service (Publisher)
    ↓
transaction-events
    ↓
├─ Fraud Engine (Subscriber)
├─ Notification Service (Subscriber)
└─ Audit Service (Subscriber)
```

### 7.2 Caching (Redis)

**Use Cases:**
- User profile cache
- Session storage
- Rate limiting counters
- Fraud rule cache

---

## 8. Deployment Architecture

### 8.1 Docker Compose Services

```yaml
Services:
├─ zookeeper:2181      (Kafka dependency)
├─ kafka:9092          (Event streaming)
├─ postgres:5432       (Database)
├─ redis:6379          (Cache)
├─ api-gateway:8080    (Gateway)
├─ auth-service:8081   (Auth)
├─ transaction:8082    (Transactions)
├─ fraud-engine:8083   (Fraud detection)
├─ notification:8084   (Notifications)
├─ user-service:8085   (User mgmt)
└─ audit-service:8086  (Audit logs)

Frontend:
└─ react:3000          (Web UI)

ML Service:
└─ ml-model:5000       (Python FastAPI)
```

### 8.2 Networking

```
┌─────────────────────────────────────────┐
│  Docker Network: fraud-detection-net    │
│                                         │
│  All services communicate via hostname  │
│  auth-service:8081                      │
│  transaction-service:8082               │
│  etc.                                   │
└─────────────────────────────────────────┘
```

---

## 9. Error Handling & Resilience

### 9.1 Circuit Breaker States

```
CLOSED (Normal)
    ↓ (50% failures detected)
OPEN (Service down)
    ↓ (10 sec timeout)
HALF_OPEN (Testing recovery)
    ↓ (if recovered) or (if failed)
    CLOSED or back to OPEN
```

### 9.2 Fallback Responses

```json
{
  "status": "SERVICE_UNAVAILABLE",
  "message": "Service is temporarily unavailable",
  "timestamp": "2026-05-30T12:00:00"
}
```

---

## 10. Scalability Considerations

### 10.1 Horizontal Scaling

- Each microservice can scale independently
- API Gateway handles load balancing
- Kafka enables asynchronous processing
- Redis cache reduces database load
- Database read replicas for scaling reads

### 10.2 Performance Optimization

- Database indexes on frequently queried fields
- Caching layer for user profiles
- Async event processing via Kafka
- Connection pooling in microservices
- Query optimization in audit service

---

## 11. Monitoring & Observability

### 11.1 Metrics Collection

- Spring Boot Actuator endpoints
- Micrometer for metrics
- Prometheus-compatible format
- Health checks per service

### 11.2 Tracing

- Request ID in headers
- Distributed tracing with Brave/Zipkin
- Log aggregation ready
- Error tracking capability

---

## 12. Development Workflow

### 12.1 Local Development

```bash
# 1. Build all services
mvn clean package -DskipTests

# 2. Start services
# Terminal 1: api-gateway
# Terminal 2: auth-service
# Terminal 3: transaction-service
# ... (7 terminals for 7 services)

# 3. Start frontend
cd frontend && npm start

# 4. Access at localhost:3000
```

### 12.2 Docker Deployment

```bash
# Build and run all services
docker-compose up -d

# Scale a service
docker-compose up -d --scale transaction-service=3

# View logs
docker-compose logs -f auth-service
```

---

## 13. Testing Strategy

### 13.1 Unit Testing

- JUnit for Java services
- Mock fraud detection rules
- Test risk scoring algorithm
- Password hashing verification

### 13.2 Integration Testing

- API Gateway routing
- Auth flow end-to-end
- Transaction fraud detection
- Kafka event flow

### 13.3 Performance Testing

- Load testing with JMeter
- Concurrent transaction processing
- Database query optimization
- Cache hit ratios

---

## 14. Future Enhancements

- [ ] ML model improvement with more training data
- [ ] Real-time analytics dashboard
- [ ] Device fingerprinting enhancement
- [ ] Behavioral biometrics
- [ ] Graph-based fraud detection
- [ ] Kubernetes deployment
- [ ] Multi-region replication
- [ ] Rate limiting per user
- [ ] Advanced threat detection
- [ ] Integration with external fraud services

---

## 15. Conclusion

This fraud detection system provides a robust, scalable architecture for real-time transaction monitoring. The microservices design allows independent scaling, the event-driven architecture ensures responsiveness, and the comprehensive audit logging provides compliance and security.

**Key Strengths:**
✅ Real-time fraud detection  
✅ Modular microservices architecture  
✅ Event-driven scalability  
✅ Comprehensive audit trail  
✅ High availability with circuit breakers  
✅ ML integration for advanced detection  
✅ Flexible risk scoring rules  

---

**Document Version:** 1.0  
**Last Updated:** May 30, 2026  
**Reviewed by:** Development Team
