# Online Banking Fraud Detection System

## Project Overview

This project is a real-time online banking fraud detection system using:

- Spring Boot Microservices
- Apache Kafka
- PostgreSQL
- React Frontend
- Python Machine Learning Model
- Docker

The system detects suspicious banking transactions using:
- Rule-based detection
- Machine Learning models
- Risk scoring
- OTP verification

---

# Architecture

```text
Client
   ↓
API Gateway
   ↓
Transaction Service
   ↓
Kafka Event Streaming
   ↓
Fraud Detection Engine
   ↓
Decision Manager
   ↓
Approve / OTP / Block
   ↓
Notification + Audit Logs
```

---

# Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React.js |
| Backend | Spring Boot |
| Database | PostgreSQL |
| Streaming | Apache Kafka |
| Cache | Redis |
| ML | Python, Scikit-learn |
| Container | Docker |
| Deployment | Kubernetes |

---

# Project Structure

```bash
fraud-detection-system/
│
├── api-gateway/
├── auth-service/
├── transaction-service/
├── fraud-engine/
├── notification-service/
├── frontend/
├── ml-model/
├── docker-compose.yml
└── README.md
```

---

# Step-by-Step Setup

## 1. Install Software

Install:

- Java 17+
- Node.js
- Maven
- Docker Desktop
- PostgreSQL
- Python 3.11+
- Apache Kafka

---

# API Gateway

## Features

- Authentication
- Rate Limiting
- Request Routing
- SSL Termination

## application.yml

```yaml
server:
  port: 8080

spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: http://localhost:8081
          predicates:
            - Path=/auth/**
```

---

# Auth Service

## Features

- Login
- JWT Authentication
- OTP Verification

## Login API

```http
POST /auth/login
```

## Sample Controller

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String login() {
        return "JWT_TOKEN";
    }
}
```

---

# Transaction Service

## Features

- Money Transfer
- Transaction History
- Kafka Event Publishing

## Transaction API

```http
POST /transaction/transfer
```

## Sample Model

```java
public class Transaction {

    private Long id;
    private String sender;
    private String receiver;
    private Double amount;
}
```

---

# Kafka Integration

## Topics

```bash
transaction-events
fraud-alerts
notification-events
audit-logs
```

## Kafka Producer

```java
kafkaTemplate.send("transaction-events", event);
```

---

# Fraud Detection Engine

## Rule-Based Detection

### Rules

| Condition | Risk Score |
|---|---|
| New Device | +30 |
| Large Amount | +40 |
| Foreign Location | +50 |

## Decision Matrix

| Score | Decision |
|---|---|
| 0-40 | APPROVE |
| 41-70 | OTP |
| 71-100 | BLOCK |

## Fraud Controller

```java
if(riskScore <= 40) {
   return "APPROVED";
}
else if(riskScore <= 70) {
   return "OTP_VERIFICATION";
}
else {
   return "BLOCKED";
}
```

---

# Machine Learning Model

## Technology

- Python
- Scikit-learn
- Isolation Forest

## Training Example

```python
from sklearn.ensemble import IsolationForest

model = IsolationForest()
model.fit(X_train)
```

## Save Model

```python
joblib.dump(model, 'fraud_model.pkl')
```

---

# FastAPI ML Service

## API

```http
POST /predict
```

## Example

```python
@app.post('/predict')
def predict(amount: float):
    prediction = model.predict([[amount]])
```

---

# Notification Service

## Features

- Email Alerts
- SMS Alerts
- OTP Notifications

## API

```http
POST /notify/send
```

---

# Frontend

## Pages

- Login
- Dashboard
- Transaction Page
- Fraud Alerts

## React Example

```javascript
const response = await axios.post(
  'http://localhost:8082/transaction/transfer',
  {
    amount: amount
  }
);
```

---

# Docker Setup

## docker-compose.yml

```yaml
version: '3'

services:

  postgres:
    image: postgres

  kafka:
    image: confluentinc/cp-kafka
```

## Run Containers

```bash
docker-compose up
```

---

# Database Tables

## users

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(255)
);
```

## transactions

```sql
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    amount DECIMAL,
    status VARCHAR(50),
    risk_score INT
);
```

---

# Security Features

- JWT Authentication
- BCrypt Password Encoding
- HTTPS
- Rate Limiting
- SQL Injection Protection

---

# Testing

## Tools

- Postman
- JUnit
- Mockito
- JMeter

## Test Cases

- Normal Transaction
- Fraud Transaction
- OTP Validation
- High Load Traffic

---

# Deployment

## Options

- AWS
- Azure
- GCP
- Kubernetes

---

# Future Enhancements

- AI Analytics Dashboard
- Device Fingerprinting
- Graph-Based Fraud Detection
- Real-Time Monitoring
- Blockchain Audit Logs

---

# Recommended Development Order

1. Auth Service
2. Transaction Service
3. Kafka Integration
4. Fraud Rule Engine
5. Frontend
6. ML Model
7. Docker Deployment

---

# Final Notes

Start with a Minimum Viable Product (MVP):

- Login System
- Transaction APIs
- Rule-Based Fraud Detection
- Kafka Integration
- Basic Dashboard

After MVP:
- Add ML Models
- Add Kubernetes
- Add Monitoring
