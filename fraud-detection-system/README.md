# Online Banking Fraud Detection System

## 1. Project Goal

Build a real-time online banking fraud detection system using:

- Rule-based fraud detection
- Machine Learning models
- Microservices architecture
- Event streaming with Kafka
- Secure authentication and OTP verification

The system should:

- Monitor banking transactions in real time
- Detect suspicious activity
- Assign risk scores
- Approve, challenge (OTP), or block transactions
- Maintain logs and alerts

## 2. Recommended Tech Stack

### Frontend

- React.js
- Tailwind CSS / Material UI

### Backend

- Java + Spring Boot
- Spring Security
- Spring Cloud Gateway

### Database

- PostgreSQL or MySQL

### Streaming

- Apache Kafka

### Cache

- Redis

### Machine Learning

- Python
- Scikit-learn
- TensorFlow (optional)

### Containerization

- Docker
- Docker Compose

### Deployment

- Kubernetes (later stage)

## 3. System Modules

Services in this repository:

- `api-gateway`
- `auth-service`
- `transaction-service`
- `user-service`
- `notification-service`
- `fraud-engine`
- `audit-service`
- `frontend`
- `ml-model`

## 4. Project Folder Structure

```bash
fraud-detection-system/
│
├── api-gateway/
├── auth-service/
├── transaction-service/
├── user-service/
├── notification-service/
├── fraud-engine/
├── audit-service/
├── frontend/
├── ml-model/
├── docker-compose.yml
└── README.md
```

## 5. Quick Start

1. Install required software:
   - Java 17+
   - Node.js
   - Maven
   - Docker Desktop
   - PostgreSQL
   - Apache Kafka
   - Python 3.11+

2. Build and run:

```bash
docker compose up --build
```

3. Open frontend:

```text
http://localhost:3000
```

## 6. Notes

- Each backend service is a Spring Boot application.
- `api-gateway` routes requests to individual microservices.
- `ml-model` is a Python FastAPI service for inference.
- Kafka topics are handled by the streaming layer.
- `user-service` and `audit-service` are added for profile and logging flows.
