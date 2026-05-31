# System Flow Diagrams
## Online Banking Fraud Detection System

---

## 1. Complete System Architecture Diagram

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃                          CLIENT LAYER                                ┃
┃                                                                      ┃
┃  ┌──────────────────────────────────────────────────────────────┐   ┃
┃  │                    React Frontend                            │   ┃
┃  │                   (Port 3000)                                │   ┃
┃  │                                                              │   ┃
┃  │   ┌─────────────┐  ┌────────────────┐  ┌──────────────────┐ │   ┃
┃  │   │ Login Page  │→ │ Dashboard Page │→ │ Transaction Form│ │   ┃
┃  │   └─────────────┘  └────────────────┘  └──────────────────┘ │   ┃
┃  │        ↓                   ↓                     ↓            │   ┃
┃  │        ├─────────── Redux Store ─────────────┤               │   ┃
┃  │        │  (Auth state, User data)            │               │   ┃
┃  │        └────────────────────────────────────┘               │   ┃
┃  └──────────────────────────────────────────────────────────────┘   ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
                              ↓ HTTPS/REST
                    (Authorization: Bearer Token)
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃                      API GATEWAY LAYER                               ┃
┃                                                                      ┃
┃         ┌────────────────────────────────────────┐                  ┃
┃         │      Spring Cloud Gateway (8080)       │                  ┃
┃         │                                        │                  ┃
┃         │  ✓ Request Routing                     │                  ┃
┃         │  ✓ Circuit Breaker (per service)       │                  ┃
┃         │  ✓ Token Validation                    │                  ┃
┃         │  ✓ Rate Limiting Ready                 │                  ┃
┃         │  ✓ Fallback Handler                    │                  ┃
┃         └─────────────────┬──────────────────────┘                  ┃
┃                           │                                         ┃
┃         ┌─────────────────┼─────────────────┐                       ┃
┃         │                 │                 │                       ┃
┗━━━━━━━━━┃━━━━━━━━━━━━━━━┃━━━━━━━━━━━━━━━┃━━━━━━━━━━━━━━━━━━━━━━┛
          │                 │                 │
    Routes: /auth/**   /transaction/**   /fraud/**
          │                 │                 │
          ↓                 ↓                 ↓
┏━━━━━━━━━━━━━┓    ┏━━━━━━━━━━━━━━━┓   ┏━━━━━━━━━━━┓
┃  Auth       ┃    ┃ Transaction   ┃   ┃ Fraud     ┃
┃  Service    ┃    ┃ Service       ┃   ┃ Engine    ┃
┃  (8081)     ┃    ┃ (8082)        ┃   ┃ (8083)    ┃
┗━━━━━━━━━━━━━┛    ┗━━━━━━━━━━━━━━━┛   ┗━━━━━━━━━━━┛
          │                 │                 │
          └─────────────────┼─────────────────┤
                            ↓                 ↓
                   ┌──────────────┐   ┌──────────────┐
                   │  PostgreSQL  │   │  Kafka Bus   │
                   │  Database    │   │ (Events)     │
                   └──────────────┘   └──────────────┘
```

---

## 2. Authentication & Login Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                         LOGIN FLOW                                  │
└─────────────────────────────────────────────────────────────────────┘

START
  │
  ↓
┌─────────────────────────────────┐
│ User opens app                  │
│ (localhost:3000)                │
└────────┬────────────────────────┘
         │
         ↓
    ┌─────────────────────────────────┐
    │ Is token in localStorage?       │
    └────┬────────────────┬───────────┘
         │ YES            │ NO
         ↓                ↓
    ┌──────────┐   ┌─────────────────┐
    │Dashboard │   │ Redirect to     │
    │ (skip)   │   │ Login Page      │
    └──────────┘   └────────┬────────┘
                            │
                            ↓
                   ┌──────────────────────────────┐
                   │ LoginPage Component          │
                   │ - Input: username, password  │
                   │ - Submit button              │
                   └────────┬─────────────────────┘
                            │
                            ↓
         ┌──────────────────────────────────────┐
         │ Redux Action: login()                │
         │ Body: {username, password}           │
         └────────┬─────────────────────────────┘
                  │
    ┌─────────────┴─────────────┐
    │ HTTP POST                 │
    │ /auth/login               │
    │ via API Gateway (8080)    │
    └─────────────┬─────────────┘
                  │
                  ↓
         ┌─────────────────────────┐
         │ Auth Service (8081)     │
         │ - Validate credentials  │
         │ - Hash & compare pwd    │
         │ - Create JWT token      │
         └────────┬────────────────┘
                  │
    ┌─────────────┴──────────────┐
    │ Success                    │
    └─────────────┬──────────────┘
                  │
                  ↓
         ┌──────────────────────────┐
         │ Return: {token, user}    │
         └────────┬─────────────────┘
                  │
                  ↓
         ┌──────────────────────────┐
         │ Redux: login({token, user})
         │ localStorage.setItem()   │
         └────────┬─────────────────┘
                  │
                  ↓
         ┌──────────────────────────┐
         │ Set isAuthenticated=true │
         │ Redirect to /dashboard   │
         └────────┬─────────────────┘
                  │
                  ↓
         ┌──────────────────────────┐
         │ Dashboard Page Ready     │
         │ (All APIs use Bearer)    │
         └──────────────────────────┘

END
```

---

## 3. Transaction Submission & Fraud Detection Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│               TRANSACTION & FRAUD DETECTION FLOW                    │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│ PHASE 1: TRANSACTION SUBMISSION                                     │
└─────────────────────────────────────────────────────────────────────┘

START
  │
  ↓
┌──────────────────────────────┐
│ User fills form:             │
│ - Amount                     │
│ - Merchant                   │
│ - Description                │
└────────┬─────────────────────┘
         │
         ↓
    ┌────────────────────────────────┐
    │ Frontend: handleSubmitTransaction
    │ Headers: Authorization: Bearer  │
    └────────┬─────────────────────────┘
             │
             ↓
    ┌────────────────────────────────┐
    │ POST /transaction/transfer     │
    │ Via API Gateway (Port 8080)    │
    └────────┬─────────────────────────┘
             │
             ↓
    ┌────────────────────────────────┐
    │ API Gateway                    │
    │ - Validate token               │
    │ - Route to /transaction/**     │
    │ - Circuit Breaker check        │
    └────────┬─────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Transaction Service (8082)         │
    │                                    │
    │ 1. Parse request                   │
    │ 2. Validate amount > 0             │
    │ 3. Validate merchant not null      │
    │ 4. Generate transaction_id (UUID)  │
    │ 5. Store in database               │
    └────────┬───────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Publish to Kafka                   │
    │                                    │
    │ Topic: transaction-events          │
    │ Message: {                         │
    │   transactionId: "tx_xyz",         │
    │   userId: "user_123",              │
    │   amount: 1000,                    │
    │   merchant: "Amazon",              │
    │   timestamp: now()                 │
    │ }                                  │
    └────────┬───────────────────────────┘
             │
             ↓
    ┌──────────────────────────────────┐
    │ Return Acknowledgment             │
    │ {"status": "TRANSFER_RECEIVED"}   │
    └────────┬──────────────────────────┘
             │
             ↓
    ┌──────────────────────────────────┐
    │ Frontend receives response        │
    │ Display: "Transaction received"   │
    └──────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│ PHASE 2: FRAUD DETECTION (ASYNC via Kafka)                         │
└─────────────────────────────────────────────────────────────────────┘

START (Fraud Engine listening)
  │
  ↓
┌─────────────────────────────────────┐
│ Kafka Consumer: transaction-events  │
│ Fraud Engine (8083)                 │
│ - Receive event                     │
│ - Parse transaction_id, amount, etc │
└────────┬────────────────────────────┘
         │
         ↓
    ┌──────────────────────────────┐
    │ RISK SCORING ENGINE          │
    │                              │
    │ score = 0                    │
    │                              │
    │ Rule 1: Amount Check         │
    │ ├─ if (amount > $5000)       │
    │ │    score += 40             │
    │ └─ else score += 0           │
    │                              │
    │ Rule 2: Device Check         │
    │ ├─ Query User Service API    │
    │ │  GET /user/profile         │
    │ ├─ if (new_device detected)  │
    │ │    score += 30             │
    │ └─ else score += 0           │
    │                              │
    │ Rule 3: Location Check       │
    │ ├─ Get user_location         │
    │ ├─ if (foreign_country)      │
    │ │    score += 50             │
    │ └─ else score += 0           │
    │                              │
    │ TOTAL: Final Score           │
    └────────┬─────────────────────┘
             │
             ↓
    ┌─────────────────────────────────┐
    │ DECISION MATRIX                 │
    └────────┬────────────────────────┘
             │
    ┌────────┴────────┬────────────┬────────────┐
    │                 │            │            │
    v                 v            v            v
┌─────────┐     ┌──────────┐ ┌──────────┐ ┌────────┐
│score≤40 │     │ 40<score │ │ 70<score │ │OTP Req │
│         │     │ ≤70      │ │  ≤100    │ │        │
└────┬────┘     └────┬─────┘ └────┬─────┘ └───┬────┘
     │               │            │           │
     ↓               ↓            ↓           ↓
  APPROVED       OTP_REQ       BLOCKED    ┌──────────┐
     │               │            │       │Notif Srv │
     │               │            │       │Send OTP  │
     │               │            │       │via SMS   │
     │               │            │       └──────────┘
     └───────────────┼────────────┘
                     │
                     ↓
    ┌───────────────────────────────┐
    │ Store Fraud Alert in DB       │
    │ fraud_alerts table:           │
    │ - transaction_id              │
    │ - fraud_score: 35-85          │
    │ - risk_level: LOW/MED/HIGH    │
    │ - decision: APPROVED/OTP/BLOCK│
    │ - created_at: timestamp       │
    └───────────┬────────────────────┘
                │
                ↓
    ┌──────────────────────────────┐
    │ Publish to Kafka             │
    │ Topic: fraud-alerts          │
    │ Consumers:                   │
    │ ✓ Notification Service       │
    │ ✓ Audit Service              │
    └───────┬──────────────────────┘
            │
            ↓
    ┌──────────────────────────────┐
    │ Audit Service receives       │
    │ - Logs event                 │
    │ - Event type: FRAUD_DETECTED │
    │ - Severity: INFO/WARNING/ERR │
    │ - Stores in audit_logs table │
    └──────────────────────────────┘

END
```

---

## 4. Risk Scoring Decision Tree

```
┌─────────────────────────────────────────────────────────────────┐
│                    FRAUD SCORING ALGORITHM                      │
└─────────────────────────────────────────────────────────────────┘

                            START
                             │
                             ↓
                    ┌──────────────────┐
                    │ score = 0        │
                    └────────┬─────────┘
                             │
                ┌────────────┼────────────┐
                │            │            │
                ↓            ↓            ↓
        ┌────────────┐ ┌──────────┐ ┌─────────┐
        │Amount      │ │Device    │ │Location │
        │Check       │ │Check     │ │Check    │
        └────┬───────┘ └────┬─────┘ └────┬────┘
             │              │            │
             ↓              ↓            ↓
        Amount>         NewDevice?   Foreign?
        $5000?           YES/NO      YES/NO
        YES/NO
             │              │            │
        ┌────┴┐        ┌────┴┐      ┌────┴┐
        │     │        │     │      │     │
       YES   NO       YES   NO     YES   NO
        │     │        │     │      │     │
        ↓     ↓        ↓     ↓      ↓     ↓
       +40    0       +30    0     +50    0
        │     │        │     │      │     │
        └─────┴────────┴─────┴──────┴─────┘
                      │
                      ↓
            ┌──────────────────────┐
            │  FINAL SCORE = SUM   │
            │  (Range: 0 - 120)    │
            └────────┬─────────────┘
                     │
      ┌──────────────┼──────────────┐
      │              │              │
      ↓              ↓              ↓
   ≤ 40           41-70           > 70
      │              │              │
      ↓              ↓              ↓
  ┌─────────┐  ┌──────────┐  ┌──────────┐
  │APPROVED │  │OTP_REQ   │  │ BLOCKED  │
  │         │  │          │  │          │
  │✓ Allow  │  │? Ask OTP │  │✗ Reject │
  │trans    │  │Send SMS  │  │trans     │
  └─────────┘  └──────────┘  └──────────┘
      │              │              │
      └──────────────┼──────────────┘
                     │
                     ↓
            ┌──────────────────┐
            │ Return Decision  │
            │ to Frontend      │
            └──────────────────┘
                     │
                     ↓
                   END
```

---

## 5. OTP Verification Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    OTP VERIFICATION FLOW                        │
└─────────────────────────────────────────────────────────────────┘

Fraud Engine Decision: OTP_REQUIRED (score 41-70)
                             │
                             ↓
                    ┌──────────────────────┐
                    │ Notification Service │
                    │ Receives alert       │
                    └────────┬─────────────┘
                             │
                             ↓
                    ┌──────────────────────┐
                    │ Generate OTP Code    │
                    │ (6-digit, random)    │
                    └────────┬─────────────┘
                             │
                             ↓
                    ┌──────────────────────┐
                    │ Send OTP to User     │
                    │ Via SMS/Email        │
                    └────────┬─────────────┘
                             │
      ┌──────────────────────┴──────────────────────┐
      │                                             │
      ↓                                             ↓
  User receives                            Frontend displays
  SMS with OTP                           "Enter OTP" dialog
      │                                      │
      └──────────────────┬───────────────────┘
                         │
                         ↓
            ┌────────────────────────┐
            │ User enters OTP code   │
            │ (within 5 min window)  │
            └────────┬───────────────┘
                     │
                     ↓
        ┌──────────────────────────┐
        │ POST /auth/verify-otp    │
        │ Body: {otp, tx_id}       │
        │ Auth Service (8081)      │
        └────────┬─────────────────┘
                 │
            ┌────┴────┐
            │          │
           YES        NO
            │          │
            ↓          ↓
      ┌──────────┐  ┌──────────┐
      │OTP Valid │  │OTP Failed│
      │          │  │          │
      │✓ Allow   │  │✗ Reject  │
      │trans     │  │trans     │
      └─────┬────┘  └────┬─────┘
            │            │
            └────┬───────┘
                 │
                 ↓
      ┌──────────────────────┐
      │ Return to Frontend   │
      │ {status: SUCCESS/ERR}│
      └──────────────────────┘
                 │
                 ↓
      ┌──────────────────────┐
      │ Display Result       │
      │ Transaction Complete │
      │ or Failed            │
      └──────────────────────┘
```

---

## 6. Audit Logging & Query Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                  AUDIT LOGGING FLOW                             │
└─────────────────────────────────────────────────────────────────┘

Any Service (Auth, Fraud, Notification, User)
                         │
                         ↓
                ┌──────────────────────┐
                │ Generate Audit Event │
                │                      │
                │ {                    │
                │   event_type: "...", │
                │   user_id: "...",    │
                │   transaction_id:.., │
                │   severity: "INFO",  │
                │   description: "..." │
                │ }                    │
                └────────┬─────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ↓                ↓                ↓
  ┌──────────┐    ┌──────────┐    ┌──────────┐
  │LOGIN     │    │TRANSACTION    │FRAUD     │
  │          │    │_CREATED      │_DETECTED │
  └──────────┘    └──────────┘    └──────────┘
        │                │                │
        └────────────────┼────────────────┘
                         │
                         ↓
            ┌─────────────────────────┐
            │ POST /audit/log         │
            │ Audit Service (8086)    │
            └────────┬────────────────┘
                     │
                     ↓
            ┌─────────────────────────┐
            │ Store in audit_logs DB  │
            │                         │
            │ ✓ Indexed by:           │
            │   - event_type          │
            │   - user_id             │
            │   - transaction_id      │
            │   - severity            │
            │   - created_at (DESC)   │
            └────────┬────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
        ↓                         ↓
  ┌────────────────────┐  ┌────────────────────┐
  │ Kafka Publisher    │  │ Database Stored    │
  │ audit-logs topic   │  │ audit_logs table   │
  └────────────────────┘  └────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    AUDIT QUERY FLOW                             │
└─────────────────────────────────────────────────────────────────┘

User requests audit logs via Admin Dashboard
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ↓                ↓                ↓
   ┌─────────┐     ┌──────────┐    ┌────────────┐
   │By Event │     │By User   │    │By Date     │
   │Type     │     │ID        │    │Range       │
   └────┬────┘     └────┬─────┘    └─────┬──────┘
        │               │                │
        ↓               ↓                ↓
  GET /audit/      GET /audit/      GET /audit/
  logs/event-type  logs/user        logs/date-range
  /FRAUD_DETECTED  /user_123        ?from=...&to=...
        │               │                │
        └───────────────┼────────────────┘
                        │
                        ↓
            ┌─────────────────────────┐
            │ Query audit_logs table  │
            │ (Using indexes)         │
            └────────┬────────────────┘
                     │
                     ↓
            ┌─────────────────────────┐
            │ Return filtered results │
            │ JSON Array of logs      │
            └────────┬────────────────┘
                     │
                     ↓
            ┌─────────────────────────┐
            │ Display on Dashboard    │
            │ With pagination         │
            │ & sorting               │
            └─────────────────────────┘
```

---

## 7. ML Model Prediction Flow

```
┌─────────────────────────────────────────────────────────────────┐
│              ML MODEL PREDICTION FLOW                           │
└─────────────────────────────────────────────────────────────────┘

Fraud Engine evaluating high-risk transaction
                         │
                         ↓
            ┌──────────────────────────┐
            │ Extract Features:        │
            │ 1. amount               │
            │ 2. merchant_category    │
            │ 3. transaction_time     │
            │ 4. user_velocity        │
            │ 5. is_new_device        │
            │ 6. country_mismatch     │
            │ ... (N features)        │
            └────────┬─────────────────┘
                     │
                     ↓
            ┌──────────────────────────┐
            │ Normalize features      │
            │ (Scale to 0-1 range)    │
            └────────┬─────────────────┘
                     │
                     ↓
        ┌───────────────────────────────┐
        │ HTTP POST /predict            │
        │ to ML Service                 │
        │                               │
        │ Body: {                       │
        │   "features": [0.5, 0.3, ...] │
        │ }                             │
        │                               │
        │ Headers:                      │
        │ Content-Type: application/json│
        └────────┬──────────────────────┘
                 │
                 ↓
    ┌────────────────────────────────────┐
    │ ML Model Service (Python FastAPI)  │
    │ Port: 5000 (or dedicated port)     │
    └────────┬─────────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Model Loading                      │
    │ - Load fraud_model.pkl (Joblib)    │
    │ - Isolation Forest instance        │
    │ - Pre-trained weights              │
    └────────┬─────────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Prediction                         │
    │ - model.predict(features)          │
    │ - Output: 0 (normal) or 1 (fraud)  │
    └────────┬─────────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Calculate Anomaly Score            │
    │ - decision_function() for confidence
    │ - Convert to 0-100 scale           │
    └────────┬─────────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Return Response                    │
    │ {                                  │
    │   "prediction": 0 or 1,            │
    │   "probability": 0.85,             │
    │   "anomaly_score": 75              │
    │ }                                  │
    └────────┬─────────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Fraud Engine receives result       │
    │ - Combine with rule-based score    │
    │ - Weight: ML = 40%, Rules = 60%    │
    │ - Final score: 50-85 range         │
    └────────┬─────────────────────────────┘
             │
             ↓
    ┌────────────────────────────────────┐
    │ Generate final decision            │
    │ APPROVED / OTP_REQUIRED / BLOCKED  │
    └────────────────────────────────────┘
```

---

## 8. Microservices Communication Pattern

```
┌─────────────────────────────────────────────────────────────────┐
│              INTER-SERVICE COMMUNICATION PATTERNS                │
└─────────────────────────────────────────────────────────────────┘

Type 1: Synchronous (REST/HTTP)
───────────────────────────────

  Fraud Engine          User Service
      │                     │
      │──── GET /user/profile ──→
      │     (Check new device)    │
      │                           │
      │ ←─── {profile, devices} ──│
      │                           │
      Decision based on response

Type 2: Asynchronous (Kafka Event Stream)
──────────────────────────────────────────

  Transaction Service
        │
        ├─→ Publish: transaction-events
        │       ├─→ Fraud Engine (Consumer)
        │       ├─→ Notification Service (Consumer)
        │       └─→ Audit Service (Consumer)
        │
        ← No blocking, fire & forget

Type 3: Database Integration
─────────────────────────────

  Multiple Services
        │
        ├─→ PostgreSQL
        │       ├─ Auth DB
        │       ├─ Fraud DB
        │       └─ Audit DB
        │
        ← Query via JDBC


Resilience Pattern
──────────────────

  API Gateway (Circuit Breaker)
        │
        ├─ Service 1 (Health)
        │   ├─ CLOSED: Normal
        │   ├─ OPEN: Fallback
        │   └─ HALF_OPEN: Testing
        │
        ├─ Service 2 (Health)
        │
        └─ Service N (Health)
```

---

## 9. Database Schema Relationships

```
┌─────────────────────────────────────────────────────────────────┐
│            DATABASE SCHEMA RELATIONSHIPS                        │
└─────────────────────────────────────────────────────────────────┘

AUTH SERVICE DATABASE
─────────────────────
┌──────────────────┐
│     users        │
├──────────────────┤
│ id (PK)          │
│ username (UNIQUE)│
│ password         │
│ email (UNIQUE)   │
│ is_active        │
│ created_at       │
│ updated_at       │
└────────┬─────────┘
         │ 1:N
         │
         ↓
┌──────────────────┐
│   sessions       │
├──────────────────┤
│ id (PK)          │
│ user_id (FK)     │
│ token (UNIQUE)   │
│ expires_at       │
│ created_at       │
└──────────────────┘

FRAUD ENGINE DATABASE
────────────────────
┌──────────────────┐
│  fraud_rules     │
├──────────────────┤
│ id (PK)          │
│ rule_name        │
│ rule_type        │
│ threshold        │
│ is_active        │
│ created_at       │
└────────┬─────────┘
         │ 1:N
         │
         ↓
┌──────────────────┐
│  fraud_alerts    │
├──────────────────┤
│ id (PK)          │
│ transaction_id   │
│ fraud_score      │
│ risk_level       │
│ rule_id (FK)     │
│ status           │
│ created_at       │
└──────────────────┘
         │ 1:N
         │
         ↓
┌──────────────────┐
│fraud_patterns    │
├──────────────────┤
│ id (PK)          │
│ user_id          │
│ pattern_type     │
│ detected_value   │
│ detected_at      │
└──────────────────┘

AUDIT SERVICE DATABASE
──────────────────────
┌──────────────────┐
│  audit_logs      │
├──────────────────┤
│ id (PK)          │
│ event_type (IDX) │
│ description      │
│ user_id (IDX)    │
│ transaction_id   │
│ request_id (IDX) │
│ severity (IDX)   │
│ created_at (IDX) │
└──────────────────┘

ALL indexes optimized for queries:
• By event_type
• By user_id
• By transaction_id
• By severity
• By date (DESC)
```

---

## 10. Deployment & Scaling Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│            DOCKER DEPLOYMENT ARCHITECTURE                      │
└─────────────────────────────────────────────────────────────────┘

Docker Network: fraud-detection-net
(All containers communicate via hostname)

┌─────────────────────────────────────────────────────────────────┐
│                     INFRASTRUCTURE LAYER                        │
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │  Zookeeper   │  │  Postgres    │  │    Redis     │          │
│  │  :2181       │  │  :5432       │  │  :6379       │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│         │                │                  │                   │
│         └────────────────┼──────────────────┘                   │
│                          ↓                                      │
│         ┌────────────────────────────────┐                     │
│         │      Kafka Message Broker      │                     │
│         │         :9092                  │                     │
│         └────────────────────────────────┘                     │
└─────────────────────────────────────────────────────────────────┘
                              ↑
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ↓                     ↓                     ↓
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│   API GATEWAY    │ │  AUTH SERVICE    │ │ TRANSACTION SVC  │
│     :8080        │ │     :8081        │ │     :8082        │
│                  │ │                  │ │                  │
│ Circuit Breaker  │ │ JWT + OTP        │ │ Kafka Producer   │
│ Routing          │ │ Flyway Migrations│ │ Transaction mgmt │
└──────────────────┘ └──────────────────┘ └──────────────────┘
        ↓                     ↓                     ↓
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│  FRAUD ENGINE    │ │  USER SERVICE    │ │ NOTIFICATION SVC │
│     :8083        │ │     :8085        │ │     :8084        │
│                  │ │                  │ │                  │
│ Risk Scoring     │ │ Profile Management     │ Email/SMS       │
│ Rule Engine      │ │ Device Tracking  │ │ Kafka Consumer   │
└──────────────────┘ └──────────────────┘ └──────────────────┘
        ↓                     ↓                     ↓
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│  AUDIT SERVICE   │ │   ML-MODEL SVC   │ │ REACT FRONTEND   │
│     :8086        │ │    :5000         │ │     :3000        │
│                  │ │                  │ │                  │
│ Audit Logging    │ │ Isolation Forest │ │ React Router     │
│ Query & Filter   │ │ FastAPI Endpoint │ │ Redux Store      │
└──────────────────┘ └──────────────────┘ └──────────────────┘

Scaling Strategy:
─────────────────
docker-compose up -d --scale transaction-service=3
   → Runs 3 instances of transaction-service
   → Load balancer routes requests
   → Auto-recovery on failure
```

---

## Summary: Request Lifecycle

```
┌────────────────────────────────────────────────────────────────────┐
│          COMPLETE REQUEST LIFECYCLE (End-to-End)                   │
└────────────────────────────────────────────────────────────────────┘

1. USER
   └─→ Opens app (localhost:3000)

2. FRONTEND
   ├─→ Checks localStorage for token
   ├─→ If expired → redirect to login
   └─→ If valid → show dashboard

3. LOGIN FLOW
   ├─→ User enters username/password
   ├─→ POST /auth/login via API Gateway
   ├─→ Auth Service validates & issues JWT
   └─→ Frontend stores token

4. TRANSACTION SUBMISSION
   ├─→ User fills transaction form
   ├─→ POST /transaction/transfer (with JWT)
   ├─→ API Gateway validates token & routes
   └─→ Transaction Service stores & publishes to Kafka

5. FRAUD DETECTION (Async)
   ├─→ Fraud Engine consumes Kafka event
   ├─→ Calculates risk score (0-100)
   ├─→ Queries User Service for device info
   ├─→ Calls ML model for prediction
   └─→ Makes decision: APPROVED/OTP/BLOCKED

6. RESPONSE HANDLING
   ├─→ If APPROVED: Show success message
   ├─→ If OTP: Notification Service sends OTP
   ├─→ User enters OTP in frontend
   └─→ If BLOCKED: Show error message

7. AUDIT LOGGING
   ├─→ Every action logged to Audit Service
   ├─→ Stored in audit_logs database
   ├─→ Queryable by event/user/transaction/date
   └─→ Available in admin dashboard

8. NOTIFICATIONS
   ├─→ Fraud Engine publishes fraud-alert
   ├─→ Notification Service consumes
   ├─→ Sends email/SMS to user
   └─→ Logs in audit trail

9. RESPONSE TO FRONTEND
   ├─→ All updates available via API
   ├─→ Dashboard displays decision
   ├─→ User can view transaction history
   └─→ Admin can view audit logs

END ✓ Complete flow finished
```

---

**Document Generated:** May 30, 2026  
**System:** Online Banking Fraud Detection  
**Total Diagrams:** 10 comprehensive flow diagrams
