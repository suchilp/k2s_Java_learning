# Local Development Setup - Complete Guide

## Prerequisites Check

### 1. Java 17+
```powershell
java -version
# Expected: java version "17.x.x" or higher
```

### 2. Maven 3.9+
```powershell
mvn -version
# Expected: Apache Maven 3.9.x
```

### 3. Docker & Docker Compose
```powershell
docker --version
# Expected: Docker version 24.x or higher

docker-compose --version
# Expected: Docker Compose version 2.x or higher
```

### 4. Git (Optional, for version control)
```powershell
git --version
```

---

## Full Local Setup (5 Steps)

### Step 1: Start Infrastructure (PostgreSQL, Kafka, Zookeeper, Redis)

```powershell
# From project root directory
cd d:\k2s\k2s_Java_learning\fraud-detection-system

# Start all infrastructure services
docker-compose up -d zookeeper kafka postgres redis

# Verify containers are running
docker ps
# Should show: zookeeper, kafka, postgres, redis

# Check logs
docker-compose logs postgres
docker-compose logs kafka
```

**Wait 30 seconds for PostgreSQL to be ready.**

### Step 2: Verify Database Connection

```powershell
# Test PostgreSQL connectivity
docker exec -it frauddb psql -U fraud_user -d frauddb -c "SELECT version();"

# Should return: PostgreSQL version info
```

### Step 3: Build All Services

```powershell
# From project root
mvn clean package -DskipTests

# This will build:
# - api-gateway
# - auth-service
# - transaction-service
# - fraud-engine
# - notification-service
# - user-service
# - audit-service

# Expected output: BUILD SUCCESS for each service
```

**Note:** First build may take 5-10 minutes (downloading dependencies).

### Step 4: Run Services Locally (Terminal Method)

Open 7 separate PowerShell terminals and run one command in each:

**Terminal 1 - API Gateway:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\api-gateway
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# Listens on: http://localhost:8080
```

**Terminal 2 - Auth Service:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\auth-service
java -jar target/auth-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# Listens on: http://localhost:8081
```

**Terminal 3 - Transaction Service:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\transaction-service
java -jar target/transaction-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# Listens on: http://localhost:8082
```

**Terminal 4 - Fraud Engine:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\fraud-engine
java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# Listens on: http://localhost:8083
```

**Terminal 5 - Notification Service:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\notification-service
java -jar target/notification-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# Listens on: http://localhost:8084
```

**Terminal 6 - User Service:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\user-service
java -jar target/user-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# Listens on: http://localhost:8085
```

**Terminal 7 - Audit Service:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\audit-service
java -jar target/audit-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# Listens on: http://localhost:8086
```

---

## Alternative: Run All Services with Docker

**Instead of 7 terminals, run everything in Docker:**

```powershell
# From project root
docker-compose up --build

# Shows logs from all services
# Press Ctrl+C to stop all services
```

---

## Step 5: Test All Services

### Test Health Checks

```powershell
# In a new PowerShell terminal

# API Gateway
Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" | Select-Object -ExpandProperty Content

# Auth Service
Invoke-WebRequest -Uri "http://localhost:8081/actuator/health" | Select-Object -ExpandProperty Content

# Transaction Service
Invoke-WebRequest -Uri "http://localhost:8082/actuator/health" | Select-Object -ExpandProperty Content

# Fraud Engine
Invoke-WebRequest -Uri "http://localhost:8083/actuator/health" | Select-Object -ExpandProperty Content

# Notification Service
Invoke-WebRequest -Uri "http://localhost:8084/actuator/health" | Select-Object -ExpandProperty Content

# User Service
Invoke-WebRequest -Uri "http://localhost:8085/actuator/health" | Select-Object -ExpandProperty Content

# Audit Service
Invoke-WebRequest -Uri "http://localhost:8086/actuator/health" | Select-Object -ExpandProperty Content
```

**Expected Response:**
```json
{
  "status": "UP"
}
```

### Test Audit Service API

```powershell
# Log an audit event
$body = @{
    event_type = "TEST_EVENT"
    description = "Testing local setup"
    user_id = "user123"
    severity = "INFO"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8086/audit/log" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

# Should return: 201 Created with event ID
```

### Test API Gateway Routes

```powershell
# Via gateway (8080) to audit service (8086)
Invoke-WebRequest -Uri "http://localhost:8080/audit/health"
```

---

## Database Verification

### View Created Tables

```powershell
# Connect to PostgreSQL
docker exec -it frauddb psql -U fraud_user -d frauddb

# Inside psql prompt:
# List all tables
\dt

# View audit_logs table
SELECT * FROM audit_logs LIMIT 5;

# View migrations
SELECT * FROM flyway_schema_history;

# Exit
\q
```

### Check Migrations Applied

```powershell
docker exec frauddb psql -U fraud_user -d frauddb -c "SELECT version, description FROM flyway_schema_history;"

# Should show:
# V1 | Create audit logs table
# V1 | Create auth tables
# V1 | Create transaction tables
# V1 | Create fraud tables
# V1 | Create notification tables
# V1 | Create user tables
```

---

## View Logs

### Dev Mode (Terminal Output)
Logs print directly to each terminal where you ran `java -jar`

### Docker Mode
```powershell
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f auth-service
docker-compose logs -f fraud-engine
docker-compose logs -f audit-service
```

---

## Kafka Topic Verification

```powershell
# List topics
docker exec kafka kafka-topics.sh --list --bootstrap-server localhost:9092

# Monitor transaction topic
docker exec -it kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic transactions \
  --from-beginning
```

---

## Troubleshooting

### Issue: "Connection refused" on localhost:5432

**Solution:** Wait for PostgreSQL to start fully
```powershell
docker logs frauddb

# Wait for: "database system is ready to accept connections"
```

### Issue: "Port already in use"

**Solution:** Stop conflicting containers
```powershell
# Kill process using port 8080 (or specific port)
Get-NetTCPConnection -LocalPort 8080 | Select-Object -ExpandProperty OwningProcess | ForEach-Object { Stop-Process -Id $_ -Force }

# Or stop all Docker containers
docker-compose down
docker ps
```

### Issue: "Flyway migration failed"

**Solution:** Check database is running and accessible
```powershell
docker exec frauddb psql -U fraud_user -d frauddb -c "SELECT 1"

# If error, restart PostgreSQL
docker-compose restart postgres
docker-compose logs postgres
```

### Issue: Services can't communicate

**Solution (Dev Mode):**
- Each service on localhost should reach others on localhost:8080, localhost:8081, etc.
- Check firewall isn't blocking ports

**Solution (Docker Mode):**
- Services use service names (auth-service, postgres, kafka)
- Already configured in application-docker.yml

### Issue: "Build failed - missing dependencies"

**Solution:** Clean and rebuild
```powershell
mvn clean
mvn install
```

---

## Performance Tips

### 1. Parallel Build (Faster)
```powershell
mvn clean package -DskipTests -T 1C
# Builds 1 module per core (faster on multi-core systems)
```

### 2. Skip Tests on First Build
```powershell
mvn clean package -DskipTests
# Skips test suite, saves time
```

### 3. Use Docker for Infrastructure Only
```powershell
# Start infrastructure
docker-compose up -d postgres kafka zookeeper redis

# Run services locally in separate terminals
# Better for debugging with IDE breakpoints
```

### 4. Hot Reload (Spring Boot Dev Tools)
Add to each pom.xml `<dependencies>` section:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

Then run with:
```powershell
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

---

## Full Workflow Example

```powershell
# 1. Navigate to project
cd d:\k2s\k2s_Java_learning\fraud-detection-system

# 2. Start infrastructure (5-10 seconds)
docker-compose up -d postgres kafka zookeeper redis

# 3. Wait for PostgreSQL (30 seconds)
Start-Sleep -Seconds 30

# 4. Verify database
docker exec frauddb psql -U fraud_user -d frauddb -c "SELECT 1"

# 5. Build all services (5-10 minutes first time)
mvn clean package -DskipTests

# 6. Run all services together in Docker
docker-compose up --build

# 7. In another terminal, test services
Invoke-WebRequest -Uri "http://localhost:8086/actuator/health"

# 8. View logs
docker-compose logs -f
```

---

## Quick Reference - Commands

| Command | Purpose |
|---------|---------|
| `mvn clean package -DskipTests` | Build all services |
| `docker-compose up -d` | Start infrastructure |
| `docker-compose logs -f <service>` | View service logs |
| `java -jar service/target/*.jar --spring.profiles.active=dev` | Run single service locally |
| `docker ps` | List running containers |
| `docker-compose down` | Stop all services |

---

## Status Indicators (Everything Working)

✅ PostgreSQL running on localhost:5432
✅ Kafka running on localhost:9092
✅ API Gateway responding on localhost:8080
✅ Auth Service responding on localhost:8081
✅ All /actuator/health endpoints return 200 OK
✅ Flyway migrations applied successfully
✅ Services can reach each other via localhost

---

## Next: Run a Complete Transaction Flow

```powershell
# 1. Log an event through Audit Service
curl -X POST http://localhost:8086/audit/log `
  -H "Content-Type: application/json" `
  -d '{"event_type":"TRANSACTION","description":"Test","severity":"INFO"}'

# 2. Check audit logs
curl http://localhost:8086/audit/logs/event-type/TRANSACTION

# 3. Monitor Kafka events
docker exec -it kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic transactions

# 4. View database state
docker exec frauddb psql -U fraud_user -d frauddb -c "SELECT COUNT(*) FROM audit_logs;"
```

---

**Everything is configured to run locally! Start with Step 1 above. ✅**
