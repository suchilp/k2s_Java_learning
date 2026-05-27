# Audit Service - Dev Readiness Implementation

## Changes Made

### 1. **pom.xml - Enhanced Dependencies**
- ✅ Added **Spring Data JPA** for persistence
- ✅ Added **PostgreSQL driver** for database connectivity
- ✅ Added **Flyway** for database migrations
- ✅ Added **Micrometer + Brave** for distributed tracing
- ✅ Added **Actuator** for health monitoring
- ✅ Added **Lombok** for clean code annotations
- ✅ Added **Spring Boot Test** framework

### 2. **application.yml - Enhanced Configuration**
- ✅ Server port configured (8086)
- ✅ PostgreSQL datasource configuration
- ✅ JPA/Hibernate settings with PostgreSQL dialect
- ✅ Flyway migration enabled
- ✅ Health & metrics endpoints exposed
- ✅ Distributed tracing enabled
- ✅ DEBUG logging for audit service & Hibernate SQL

### 3. **application-docker.yml - Docker Profile**
- ✅ Service name for PostgreSQL (postgres instead of localhost)
- ✅ Same JPA and Flyway configurations
- ✅ Automatically activated in docker-compose.yml

### 4. **Entity Model (AuditLog.java)**
- ✅ Mapped to `audit_logs` table
- ✅ Auto-timestamp on creation (@PrePersist)
- ✅ Severity enum (INFO, WARNING, ERROR, CRITICAL)
- ✅ Indexed fields for performance (event_type, created_at)
- ✅ Lombok annotations for clean code

### 5. **Repository (AuditLogRepository.java)**
- ✅ JPA repository with custom queries
- ✅ Find by event type, transaction ID, user ID
- ✅ Date range queries
- ✅ Severity filtering

### 6. **DTO (AuditEventDTO.java)**
- ✅ Clean request/response model with @JsonProperty
- ✅ Supports JSON snake_case conversion
- ✅ Lombok-powered getters/setters

### 7. **Service Layer (AuditService.java)**
- ✅ Business logic for audit logging
- ✅ Transactional support
- ✅ Error handling with proper logging
- ✅ Query methods for retrieval
- ✅ Severity level validation

### 8. **Controller (AuditController.java)**
- ✅ POST `/audit/log` - Log new audit events
- ✅ GET `/audit/logs/event-type/{eventType}` - Filter by event type
- ✅ GET `/audit/logs/transaction/{transactionId}` - Filter by transaction
- ✅ GET `/audit/logs/user/{userId}` - Filter by user
- ✅ GET `/audit/logs/date-range` - Date range queries
- ✅ GET `/audit/logs/severity/{severity}` - Filter by severity
- ✅ GET `/audit/health` - Service health check
- ✅ Proper error handling & HTTP status codes

### 9. **Database Migration (V1__Create_audit_logs_table.sql)**
- ✅ Creates `audit_logs` table with proper schema
- ✅ Indexes on frequently queried columns
- ✅ Timestamps and default values
- ✅ Table and column comments for documentation

### 10. **docker-compose.yml - Profile Activation**
- ✅ Audit service activates `docker` profile via `SPRING_PROFILES_ACTIVE`
- ✅ Uses service name `postgres` instead of localhost

---

## API Endpoints

### Log Audit Event
```bash
POST /audit/log
Content-Type: application/json

{
  "event_type": "TRANSACTION_COMPLETED",
  "description": "User transaction processed successfully",
  "user_id": "user123",
  "transaction_id": "txn456",
  "request_id": "req789",
  "severity": "INFO"
}

Response (201 Created):
{
  "status": "SUCCESS",
  "message": "Audit event logged successfully",
  "id": 1
}
```

### Get Logs by Event Type
```bash
GET /audit/logs/event-type/TRANSACTION_COMPLETED
```

### Get Logs by Transaction ID
```bash
GET /audit/logs/transaction/txn456
```

### Get Logs by User ID
```bash
GET /audit/logs/user/user123
```

### Get Logs by Date Range
```bash
GET /audit/logs/date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```

### Get Logs by Severity
```bash
GET /audit/logs/severity/ERROR
```

### Health Check
```bash
GET /audit/health
```

---

## Local Development Setup

### Prerequisites
```bash
java -version              # Java 17+
mvn -version              # Maven 3.9+
docker --version          # Docker
docker-compose --version  # Docker Compose
```

### Database Setup (Local Dev)
```bash
# Start PostgreSQL container
docker run --name frauddb \
  -e POSTGRES_USER=fraud_user \
  -e POSTGRES_PASSWORD=fraud_pass \
  -e POSTGRES_DB=frauddb \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  -d postgres:15

# Verify connection
psql -h localhost -U fraud_user -d frauddb -c "SELECT 1"
```

### Build & Run Locally (Dev Mode)
```bash
cd audit-service
mvn clean package
java -jar target/audit-service-0.0.1-SNAPSHOT.jar
```

The app will:
- Use `application.yml` (localhost mode)
- Apply Flyway migrations automatically
- Create `audit_logs` table
- Listen on port 8086

### Test Endpoints
```bash
# Health check
curl http://localhost:8086/audit/health

# Log an event
curl -X POST http://localhost:8086/audit/log \
  -H "Content-Type: application/json" \
  -d '{
    "event_type": "TEST_EVENT",
    "description": "Test audit event",
    "severity": "INFO"
  }'

# Retrieve logs
curl http://localhost:8086/audit/logs/event-type/TEST_EVENT
```

### Run in Docker (Docker Mode)
```bash
cd ..  # From project root
docker-compose up --build audit-service postgres
```

The audit service will:
- Use `application-docker.yml` (service names)
- Connect to `postgres` service automatically
- Apply Flyway migrations
- Listen on port 8086

---

## Monitoring & Debugging

### Health Check
```bash
curl http://localhost:8086/actuator/health
```

### Metrics
```bash
curl http://localhost:8086/actuator/metrics
curl http://localhost:8086/actuator/prometheus
```

### View Database Tables
```bash
psql -h localhost -U fraud_user -d frauddb
frauddb=# \dt
frauddb=# SELECT * FROM audit_logs LIMIT 10;
frauddb=# SELECT COUNT(*) FROM audit_logs;
```

### View Logs
```bash
# Dev mode
tail -f logs/audit-service.log

# Docker mode
docker-compose logs -f audit-service
```

### Check Flyway Migrations
```bash
# Inside database
SELECT * FROM flyway_schema_history;
```

---

## Database Schema

```sql
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    user_id VARCHAR(100),
    transaction_id VARCHAR(100),
    request_id VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    severity VARCHAR(20) NOT NULL DEFAULT 'INFO'
);

-- Indexes
INDEX idx_event_type ON audit_logs(event_type);
INDEX idx_created_at ON audit_logs(created_at DESC);
INDEX idx_transaction_id ON audit_logs(transaction_id);
INDEX idx_user_id ON audit_logs(user_id);
INDEX idx_severity ON audit_logs(severity);
```

---

## Next Steps (Recommended)

1. **Add Kafka Integration**
   - Subscribe to fraud detection events
   - Log fraudulent transactions automatically

2. **Add Caching**
   - Redis cache for frequently accessed logs
   - Reduce database load

3. **Add Search**
   - Elasticsearch integration for full-text search
   - Advanced filtering

4. **Add Security**
   - JWT authentication
   - Role-based access control (RBAC)

5. **Add Archival**
   - Archive old logs to cold storage
   - Implement retention policies

6. **Add Reporting**
   - Monthly audit reports
   - Compliance dashboards

---

## Files Created/Modified

```
audit-service/
├── pom.xml                                    [UPDATED]
├── src/main/resources/
│   ├── application.yml                        [CREATED]
│   ├── application-docker.yml                 [CREATED]
│   └── db/migration/
│       └── V1__Create_audit_logs_table.sql    [CREATED]
├── src/main/java/com/example/audit/
│   ├── AuditServiceApplication.java           [UNCHANGED]
│   ├── model/
│   │   └── AuditLog.java                      [CREATED]
│   ├── repository/
│   │   └── AuditLogRepository.java            [CREATED]
│   ├── dto/
│   │   └── AuditEventDTO.java                 [CREATED]
│   ├── service/
│   │   └── AuditService.java                  [CREATED]
│   └── controller/
│       └── AuditController.java               [UPDATED]

docker-compose.yml                            [UPDATED]
```

---

## Validation Checklist

- [x] JPA persistence layer configured
- [x] PostgreSQL connectivity verified
- [x] Flyway migrations enabled
- [x] Database schema created
- [x] Repository queries defined
- [x] Service business logic implemented
- [x] REST API endpoints functional
- [x] Error handling implemented
- [x] Logging configured
- [x] Docker profile configured
- [x] Health monitoring enabled
- [x] Actuator endpoints exposed
- [x] Transactional support added
- [x] DTOs with proper serialization

**Status: ✅ Audit Service is now dev-ready!**
