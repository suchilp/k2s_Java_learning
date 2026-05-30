# Run Locally - End-to-End Setup

## ✅ System Status

- **Kafka**: Running (port 9092)
- **Zookeeper**: Running (port 2181)
- **Backend Services**: Built ✓
  - auth-service (8081)
  - transaction-service (8082)
  - fraud-engine (8083)
  - notification-service (8084)
  - user-service (8085)
- **Frontend**: Node dependencies installed ✓
- **Java 26, Maven 3.9, Node v26** installed

---

## Quick Start

### Terminal 1: Start Auth Service
```bash
cd auth-service
mvn spring-boot:run
```

### Terminal 2: Start Transaction Service
```bash
cd transaction-service
mvn spring-boot:run
```

### Terminal 3: Start Fraud Engine
```bash
cd fraud-engine
mvn spring-boot:run
```

### Terminal 4: Start Notification Service
```bash
cd notification-service
mvn spring-boot:run
```

### Terminal 5: Start User Service
```bash
cd user-service
mvn spring-boot:run
```

### Terminal 6: Start Frontend
```bash
cd frontend
npm start
```

---

## Access the System

Once all services are running:

- **Frontend**: http://localhost:3000
- **Auth Service Health**: http://localhost:8081/actuator/health
- **Transaction Service Health**: http://localhost:8082/actuator/health
- **Fraud Engine Health**: http://localhost:8083/actuator/health
- **Notification Service Health**: http://localhost:8084/actuator/health
- **User Service Health**: http://localhost:8085/actuator/health

---

## Verify Services Running

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
```

All should return `{"status":"UP"}`.

---

## Kafka Topics Created

Available topics:
- `transactions`
- `fraud-alerts`
- `notifications`

Monitor events:
```bash
kafka-topics --list --bootstrap-server localhost:9092
kafka-console-consumer --topic transactions --from-beginning --bootstrap-server localhost:9092
```

---

## Stop All Services

- Press `Ctrl+C` in each terminal running a service
- Kafka and Zookeeper will stay running as Homebrew services

To stop Kafka/Zookeeper:
```bash
brew services stop kafka
brew services stop zookeeper
```

---

## Notes

- Each service uses **H2 in-memory database** - no PostgreSQL needed
- **Kafka** handles async messaging between services
- Services are already **built** and ready to run
- All services configured for **localhost:9092** (Kafka)

---

## Service Ports

| Service | Port |
|---------|------|
| Auth Service | 8081 |
| Transaction Service | 8082 |
| Fraud Engine | 8083 |
| Notification Service | 8084 |
| User Service | 8085 |
| Frontend | 3000 |

---

## Build Status

✅ Built successfully:
- auth-service
- transaction-service
- fraud-engine
- notification-service
- user-service

⚠️ Build issues (Java/Lombok compatibility):
- api-gateway
- audit-service

These can be addressed later with proper Lombok/Java version alignment.
