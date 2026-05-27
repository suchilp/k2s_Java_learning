# Run Without Docker - Complete Local Setup

## ✅ YES, You Can Run Everything Without Docker

This guide shows how to run all 7 microservices on your local machine using only:
- Java 17+
- Maven 3.9+
- PostgreSQL (standalone)
- Kafka (standalone)

**No Docker required!**

---

## Prerequisites Setup

### 1. Install PostgreSQL (Local Database)

#### Option A: Windows Installer (Easiest)
1. Download from: https://www.postgresql.org/download/windows/
2. Run installer for PostgreSQL 15 or higher
3. Choose installation directory (e.g., `C:\Program Files\PostgreSQL\15`)
4. Set password for `postgres` user (remember it!)
5. Choose port 5432 (default)
6. Complete installation

#### Option B: PostgreSQL without Installer
1. Download portable ZIP from: https://www.postgresql.org/download/windows/
2. Extract to folder (e.g., `C:\postgresql`)
3. Initialize database

**Verify Installation:**
```powershell
# Test PostgreSQL connection
psql -U postgres -h localhost

# If connected, type: \q (to exit)
```

### 2. Install Kafka (Message Broker)

**Option A: Pre-built Binary (Recommended)**

1. Download Kafka from: https://kafka.apache.org/downloads
   - Choose latest stable version (e.g., 3.6.0)
   - Download binary: `kafka_2.13-3.6.0.tgz`

2. Extract to folder:
   ```powershell
   # Create folder
   mkdir C:\kafka
   
   # Extract downloaded file to C:\kafka
   # You should have: C:\kafka\bin, C:\kafka\config, etc.
   ```

3. Verify folder structure:
   ```powershell
   ls C:\kafka\bin
   # Should show: windows, unix folders
   ```

**Option B: Using WSL (Windows Subsystem for Linux)**
```powershell
# If WSL2 installed:
wsl
tar xzf kafka_2.13-3.6.0.tgz -C ~/kafka/
```

---

## Create Local Databases & Tables

### Step 1: Connect to PostgreSQL

```powershell
# Connect to PostgreSQL with default user
psql -U postgres -h localhost

# You'll see: postgres=#
```

### Step 2: Create Database

```sql
-- Inside psql prompt:
CREATE DATABASE frauddb;
CREATE USER fraud_user WITH PASSWORD 'fraud_pass';
GRANT ALL PRIVILEGES ON DATABASE frauddb TO fraud_user;
\c frauddb
```

### Step 3: Create Tables (Flyway Migrations)

```sql
-- ===== AUDIT LOGS TABLE =====
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
CREATE INDEX idx_event_type ON audit_logs(event_type);
CREATE INDEX idx_created_at ON audit_logs(created_at DESC);

-- ===== AUTH TABLES =====
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(500) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_sessions_token ON sessions(token);

-- ===== TRANSACTION TABLES =====
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    status VARCHAR(50) DEFAULT 'PENDING',
    description VARCHAR(500),
    merchant VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE transaction_events (
    id BIGSERIAL PRIMARY KEY,
    transaction_id BIGINT NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL,
    details VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_transactions_user_id ON transactions(user_id);

-- ===== FRAUD TABLES =====
CREATE TABLE fraud_rules (
    id BIGSERIAL PRIMARY KEY,
    rule_name VARCHAR(255) NOT NULL,
    rule_description VARCHAR(1000),
    rule_type VARCHAR(50) NOT NULL,
    threshold DECIMAL(10,2),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE fraud_alerts (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(100) NOT NULL,
    fraud_score DECIMAL(5,2) NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    rule_id BIGINT REFERENCES fraud_rules(id),
    status VARCHAR(50) DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE fraud_patterns (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    pattern_type VARCHAR(100) NOT NULL,
    detected_value VARCHAR(500),
    detected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_fraud_alerts_tx_id ON fraud_alerts(transaction_id);

-- ===== USER PROFILE TABLES =====
CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_user_profiles_user_id ON user_profiles(user_id);

-- ===== NOTIFICATION TABLES =====
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message VARCHAR(2000) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);

-- Exit psql
\q
```

### Step 4: Verify Tables Created

```powershell
psql -U fraud_user -d frauddb -h localhost

# Inside psql:
frauddb=> \dt
# Should show all tables created above

frauddb=> \q
```

---

## Configure Services (No Docker Needed)

### Update application.yml Files

Each service already has `application.yml` configured for localhost. **No changes needed!** 

Current config already points to:
- PostgreSQL: `jdbc:postgresql://localhost:5432/frauddb`
- Kafka: `bootstrap-servers: localhost:9092`

Verify each file:

```powershell
# Check auth-service config
cat auth-service/src/main/resources/application.yml

# You should see:
# datasource:
#   url: jdbc:postgresql://localhost:5432/frauddb
#   username: fraud_user
#   password: fraud_pass
# kafka:
#   bootstrap-servers: localhost:9092
```

---

## Start Kafka (No Docker)

### Step 1: Start Zookeeper

```powershell
# Open PowerShell as Administrator
# Navigate to Kafka directory
cd C:\kafka

# Start Zookeeper
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

# Wait for: [INFO] binding to port 0.0.0.0/0.0.0.0:2181 (ZooKeeperServer)
# Leave this terminal open
```

### Step 2: Start Kafka (New Terminal)

```powershell
# Open a NEW PowerShell terminal
cd C:\kafka

# Start Kafka broker
.\bin\windows\kafka-server-start.bat .\config\server.properties

# Wait for: [INFO] Started socket server at 127.0.0.1:9092
# Leave this terminal open
```

### Step 3: Create Kafka Topics

```powershell
# Open ANOTHER NEW PowerShell terminal
cd C:\kafka

# Create topics
.\bin\windows\kafka-topics.bat --create --topic transactions --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
.\bin\windows\kafka-topics.bat --create --topic fraud-alerts --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
.\bin\windows\kafka-topics.bat --create --topic notifications --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

# Verify topics
.\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
# Should show: transactions, fraud-alerts, notifications
```

---

## Build All Services

```powershell
# Navigate to project root
cd d:\k2s\k2s_Java_learning\fraud-detection-system

# Build all services
mvn clean package -DskipTests

# Expected: BUILD SUCCESS (7 times, one for each service)
```

---

## Run All Services Locally (7 Terminals)

Open 7 separate PowerShell windows and run one in each:

### Terminal 1: API Gateway (8080)
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\api-gateway
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Wait for: Started ApiGatewayApplication in X.XXX seconds
```

### Terminal 2: Auth Service (8081)
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\auth-service
java -jar target/auth-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Wait for: Started AuthServiceApplication in X.XXX seconds
```

### Terminal 3: Transaction Service (8082)
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\transaction-service
java -jar target/transaction-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Wait for: Started TransactionServiceApplication in X.XXX seconds
```

### Terminal 4: Fraud Engine (8083)
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\fraud-engine
java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Wait for: Started FraudEngineApplication in X.XXX seconds
```

### Terminal 5: Notification Service (8084)
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\notification-service
java -jar target/notification-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Wait for: Started NotificationServiceApplication in X.XXX seconds
```

### Terminal 6: User Service (8085)
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\user-service
java -jar target/user-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Wait for: Started UserServiceApplication in X.XXX seconds
```

### Terminal 7: Audit Service (8086)
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system\audit-service
java -jar target/audit-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Wait for: Started AuditServiceApplication in X.XXX seconds
```

---

## Verify Everything is Running

Open a new PowerShell terminal and run:

```powershell
# Test all services
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
curl http://localhost:8086/actuator/health

# All should return: {"status":"UP"}
```

---

## Test the System

### Test Audit Service API

```powershell
# Create audit log
$body = @{
    event_type = "TEST_EVENT"
    description = "Testing local setup without Docker"
    user_id = "user123"
    severity = "INFO"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "http://localhost:8086/audit/log" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

Write-Host $response.Content
# Should return: {"status":"SUCCESS","message":"Audit event logged successfully","id":1}

# Query audit logs
curl http://localhost:8086/audit/logs/event-type/TEST_EVENT
```

### Check Database

```powershell
# Connect to database
psql -U fraud_user -d frauddb -h localhost

# Check if audit log was created
frauddb=> SELECT * FROM audit_logs LIMIT 1;

# Should show the event you just created

frauddb=> \q
```

### Monitor Kafka Topics

```powershell
# In a new terminal, monitor transaction topic
cd C:\kafka
.\bin\windows\kafka-console-consumer.bat --topic transactions --from-beginning --bootstrap-server localhost:9092

# When transaction service publishes events, you'll see them here
```

---

## Complete Startup Checklist

### Services to Keep Running (Don't Close These!)

- ✅ **PostgreSQL Server** - Running in background
- ✅ **Zookeeper** - Terminal 1 (running)
- ✅ **Kafka** - Terminal 2 (running)
- ✅ **API Gateway** - Terminal 3 (running)
- ✅ **Auth Service** - Terminal 4 (running)
- ✅ **Transaction Service** - Terminal 5 (running)
- ✅ **Fraud Engine** - Terminal 6 (running)
- ✅ **Notification Service** - Terminal 7 (running)
- ✅ **User Service** - Terminal 8 (running)
- ✅ **Audit Service** - Terminal 9 (running)

Total: **11 Terminal Windows/Processes**

### Simplified Diagram

```
PostgreSQL (localhost:5432) ← All 7 services connect to database
    ↓
Zookeeper (localhost:2181)
    ↓
Kafka (localhost:9092) ← Transaction/Fraud/Notification services publish/consume
    ↓
7 Microservices (8080-8086)
```

---

## Troubleshooting (No Docker)

### Issue: PostgreSQL Connection Refused

**Solution:**
```powershell
# Check if PostgreSQL is running
Get-Process postgres

# If not running, start PostgreSQL service
# Windows Start Menu → Services → PostgreSQL → Start

# Or from command line:
net start "postgresql-x64-15"  # Adjust version number as needed

# Test connection
psql -U postgres -h localhost
```

### Issue: Kafka Connection Failed

**Solution:**
```powershell
# Make sure Zookeeper is running first
# Check ports
netstat -an | findstr 2181  # Zookeeper port
netstat -an | findstr 9092  # Kafka port

# If ports not in use, restart services:
# Terminal 1: Stop Zookeeper (Ctrl+C), restart
# Terminal 2: Stop Kafka (Ctrl+C), restart
```

### Issue: "Address already in use" (Ports 8080, 8086, etc.)

**Solution:**
```powershell
# Find process using port (e.g., 8080)
Get-NetTCPConnection -LocalPort 8080 | Select-Object -ExpandProperty OwningProcess | ForEach-Object { Get-Process -Id $_ }

# Kill the process
Stop-Process -Id [PID] -Force

# Or simply use different port:
java -jar target/service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --server.port=8090
```

### Issue: Out of Memory (Services crash)

**Solution:**
```powershell
# Increase Java heap size
java -Xmx2G -Xms512M -jar target/service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
# -Xmx2G = max 2GB heap
# -Xms512M = initial 512MB heap
```

### Issue: Services can't reach each other

**Solution - Check firewall:**
```powershell
# Windows Defender Firewall might block ports
# Allow Java through firewall:
# Windows Start Menu → Windows Defender Firewall → Allow app through firewall
# Add: java.exe or javaw.exe
```

---

## Quick Reference - Commands Without Docker

| Task | Command |
|------|---------|
| Start PostgreSQL | Manual service or `net start postgresql-x64-15` |
| Start Zookeeper | `C:\kafka\bin\windows\zookeeper-server-start.bat` |
| Start Kafka | `C:\kafka\bin\windows\kafka-server-start.bat` |
| Build services | `mvn clean package -DskipTests` |
| Run service | `java -jar target/*.jar --spring.profiles.active=dev` |
| Connect to DB | `psql -U fraud_user -d frauddb -h localhost` |
| List Kafka topics | `.\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092` |
| Monitor topic | `.\bin\windows\kafka-console-consumer.bat --topic transactions --bootstrap-server localhost:9092` |

---

## Advanced: Create Windows Batch Script

Save as `start-all.bat`:

```batch
@echo off
echo Starting Fraud Detection System (No Docker)...
echo.

REM Check prerequisites
where java >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found
    exit /b 1
)

where mvn >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven not found
    exit /b 1
)

echo ✓ Java and Maven found
echo.

REM Start PostgreSQL (requires service)
echo Starting PostgreSQL...
net start "postgresql-x64-15"
timeout /t 5

REM Start Zookeeper
echo Starting Zookeeper (Terminal 1)...
start "Zookeeper" C:\kafka\bin\windows\zookeeper-server-start.bat C:\kafka\config\zookeeper.properties
timeout /t 5

REM Start Kafka
echo Starting Kafka (Terminal 2)...
start "Kafka" C:\kafka\bin\windows\kafka-server-start.bat C:\kafka\config\server.properties
timeout /t 10

REM Build services
echo Building services...
mvn clean package -DskipTests -q

REM Start all 7 services
echo Launching services...
start "API Gateway (8080)" cmd /k "cd api-gateway && java -jar target/api-gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
start "Auth Service (8081)" cmd /k "cd auth-service && java -jar target/auth-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
start "Transaction Service (8082)" cmd /k "cd transaction-service && java -jar target/transaction-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
start "Fraud Engine (8083)" cmd /k "cd fraud-engine && java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
start "Notification Service (8084)" cmd /k "cd notification-service && java -jar target/notification-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
start "User Service (8085)" cmd /k "cd user-service && java -jar target/user-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
start "Audit Service (8086)" cmd /k "cd audit-service && java -jar target/audit-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"

echo.
echo All services starting...
echo.
echo ========================================
echo Fraud Detection System (No Docker)
echo ========================================
echo API Gateway: http://localhost:8080
echo Auth Service: http://localhost:8081
echo Transaction Service: http://localhost:8082
echo Fraud Engine: http://localhost:8083
echo Notification Service: http://localhost:8084
echo User Service: http://localhost:8085
echo Audit Service: http://localhost:8086
echo.
echo Test: curl http://localhost:8086/actuator/health
echo ========================================
pause
```

**Run it:**
```powershell
cd d:\k2s\k2s_Java_learning\fraud-detection-system
.\start-all.bat
```

---

## Summary: Running Without Docker

| Component | Installation | Port | Start Method |
|-----------|--------------|------|--------------|
| PostgreSQL | Windows Installer | 5432 | Windows Service |
| Zookeeper | Kafka binary | 2181 | `zookeeper-server-start.bat` |
| Kafka | Download from kafka.apache.org | 9092 | `kafka-server-start.bat` |
| 7 Services | `mvn package` | 8080-8086 | `java -jar` × 7 |

**Total Setup Time:** ~30 minutes  
**Startup Time:** ~2 minutes (after first time)

---

## Benefits & Drawbacks

### Benefits of No Docker ✅
- Direct Java debugging
- IDE breakpoints work
- Faster startup
- See real logs
- Can modify & rebuild quickly
- No container overhead

### Drawbacks ⚠️
- Need to manually install PostgreSQL & Kafka
- More terminal windows
- More complex to clean up
- Harder to reproduce issues
- Manual port management

---

**Status: ✅ Can run everything locally WITHOUT Docker!**
