# Quick Local Setup - In-Memory Database (H2)

## ✅ No External Database Required!

The project is now configured to use **H2 in-memory database**. No need to install PostgreSQL or Docker!

---

## Prerequisites

### 1. Check Java Version
```powershell
java -version
# Expected: java version "17.x.x" or higher
```

If you don't have Java 17+, download from: https://www.oracle.com/java/technologies/downloads/

### 2. Check Maven Version
```powershell
mvn -version
# Expected: Apache Maven 3.9.x or higher
```

If you don't have Maven, download from: https://maven.apache.org/download.cgi

---

## Step 1: Build All Services

```powershell
# Navigate to project root
cd c:\k2s\k2s_Java_learning\fraud-detection-system

# Build all services
mvn clean package -DskipTests

# Expected: BUILD SUCCESS for each service
```

**Note:** First build may take 5-10 minutes (downloading dependencies).

---

## Step 2: Run Each Service (Open 6 Separate PowerShell Terminals)

### Terminal 1 - API Gateway (Port 8080)
```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\api-gateway
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar

# You should see: Started ApiGatewayApplication...
```

### Terminal 2 - Auth Service (Port 8081)
```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\auth-service
java -jar target/auth-service-0.0.1-SNAPSHOT.jar

# You should see: Started AuthServiceApplication...
```

### Terminal 3 - Transaction Service (Port 8082)
```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\transaction-service
java -jar target/transaction-service-0.0.1-SNAPSHOT.jar

# You should see: Started TransactionServiceApplication...
```

### Terminal 4 - Fraud Engine (Port 8083)
```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\fraud-engine
java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar

# You should see: Started FraudEngineApplication...
```

### Terminal 5 - Notification Service (Port 8084)
```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\notification-service
java -jar target/notification-service-0.0.1-SNAPSHOT.jar

# You should see: Started NotificationServiceApplication...
```

### Terminal 6 - User Service (Port 8085)
```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\user-service
java -jar target/user-service-0.0.1-SNAPSHOT.jar

# You should see: Started UserServiceApplication...
```

### Terminal 7 - Audit Service (Port 8086)
```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\audit-service
java -jar target/audit-service-0.0.1-SNAPSHOT.jar

# You should see: Started AuditServiceApplication...
```

---

## Step 3: Test API Gateway

All services route through the API Gateway on **port 8080**:

```powershell
# Test health check
curl http://localhost:8080/auth/health

# Or open in browser:
# http://localhost:8080/auth/health
```

---

## View H2 Console

Each service has an H2 console for viewing in-memory database:

- Auth Service: http://localhost:8081/h2-console
- User Service: http://localhost:8085/h2-console
- Transaction Service: http://localhost:8082/h2-console
- Fraud Engine: http://localhost:8083/h2-console
- Notification Service: http://localhost:8084/h2-console
- Audit Service: http://localhost:8086/h2-console

**H2 Console Login:**
- JDBC URL: `jdbc:h2:mem:frauddb`
- User: `sa`
- Password: (leave empty)

---

## Frontend (React)

If you want to run the frontend:

```powershell
cd c:\k2s\k2s_Java_learning\fraud-detection-system\frontend

# Install dependencies
npm install

# Start development server
npm start

# Opens on http://localhost:3000
```

---

## Troubleshooting

### Port Already in Use
If a port is already in use, modify the port in the service's `application.yml`:

```yaml
server:
  port: 9081  # Change from 8081 to 9081
```

### Build Fails
```powershell
# Clean maven cache
mvn clean

# Try building with more memory
$env:MAVEN_OPTS="-Xmx1024m"
mvn clean package -DskipTests
```

### Database Connection Issues
Since we're using in-memory H2, there are no connection issues! If you see errors:
1. Ensure `ddl-auto: create-drop` is in application.yml
2. Ensure `driver-class-name: org.h2.Driver` is set

---

## 🎉 Done!

All 7 microservices are now running on your local machine with in-memory database!

**Services Summary:**
- API Gateway: http://localhost:8080
- Auth Service: http://localhost:8081
- Transaction Service: http://localhost:8082
- Fraud Engine: http://localhost:8083
- Notification Service: http://localhost:8084
- User Service: http://localhost:8085
- Audit Service: http://localhost:8086
