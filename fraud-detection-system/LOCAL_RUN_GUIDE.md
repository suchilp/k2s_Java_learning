# Local Run Guide - Fraud Detection System

This guide explains how to run the entire Fraud Detection System locally without Docker.

## Prerequisites

### Java & Maven
- **Java 17+** (JDK)
- **Maven 3.8+**

Verify installation:
```bash
java -version
mvn -version
```

### Node.js
- **Node.js 16+** and **npm 8+**

Verify installation:
```bash
node -version
npm -version
```

### Python (Optional - for ML Model)
- **Python 3.8+**
- Required packages: `flask`, `numpy`, `scikit-learn`

## Database Setup

The system uses H2 in-memory database by default (no setup needed) or PostgreSQL for persistence.

### Using In-Memory H2 (Default)
No setup required. Databases are created automatically on startup.

### Using PostgreSQL (Optional)
If you want to use PostgreSQL instead:

1. Install PostgreSQL
2. Create databases:
```sql
CREATE DATABASE fraud_auth;
CREATE DATABASE fraud_transaction;
CREATE DATABASE fraud_audit;
CREATE DATABASE fraud_notification;
CREATE DATABASE fraud_user;
```

3. Update `application.yml` in each service to use PostgreSQL connection string

## Running the Services

### Step 1: Build All Services
```bash
cd c:\k2s\k2s_Java_learning\fraud-detection-system

# Build all Java services
mvn clean install -DskipTests
```

### Step 2: Start Backend Services

Open **5 separate terminals** and run each service:

**Terminal 1 - Auth Service (Port 8081)**
```bash
cd c:\k2s\k2s_Java_learning\fraud-detection-system\auth-service
mvn spring-boot:run
```

**Terminal 2 - Transaction Service (Port 8082)**
```bash
cd c:\k2s\k2s_Java_learning\fraud-detection-system\transaction-service
mvn spring-boot:run
```

**Terminal 3 - Fraud Engine (Port 8083)**
```bash
cd c:\k2s\k2s_Java_learning\fraud-detection-system\fraud-engine
mvn spring-boot:run
```

**Terminal 4 - API Gateway (Port 8080)**
```bash
cd c:\k2s\k2s_Java_learning\fraud-detection-system\api-gateway
mvn spring-boot:run
```

**Terminal 5 - Other Services (Notification, Audit, User)**
```bash
# Notification Service (Port 8084)
cd c:\k2s\k2s_Java_learning\fraud-detection-system\notification-service
mvn spring-boot:run
```

### Step 3: Start Frontend (React)

Open another terminal:
```bash
cd c:\k2s\k2s_Java_learning\fraud-detection-system\frontend

# Install dependencies (first time only)
npm install

# Start development server
npm start
```

The frontend will open automatically at: **http://localhost:3000**

### Step 4: Start ML Model Service (Optional)

Open another terminal:
```bash
cd c:\k2s\k2s_Java_learning\fraud-detection-system\ml-model

# Install Python dependencies
pip install -r requirements.txt

# Run Flask app
python app.py
```

ML Model will be available at: **http://localhost:5000**

## Service Architecture

| Service | Port | Purpose |
|---------|------|---------|
| Frontend | 3000 | React UI |
| API Gateway | 8080 | Main entry point for all APIs |
| Auth Service | 8081 | User authentication & authorization |
| Transaction Service | 8082 | Transaction management |
| Fraud Engine | 8083 | Fraud detection & scoring |
| Notification Service | 8084 | Email/SMS notifications |
| ML Model | 5000 | Machine learning predictions |

## Default Credentials

### Admin User
- **Username**: `admin`
- **Password**: `admin123`

### Test User
- **Username**: `testuser`
- **Password**: `test123`

## Testing the Application

### 1. Login
Navigate to: `http://localhost:3000/login`
- Use credentials above to login

### 2. Dashboard
After login, go to: `http://localhost:3000/dashboard`
- Submit a transaction to test fraud detection
- View fraud evaluation results

### 3. Test API Directly
```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Create Transaction
curl -X POST http://localhost:8080/transaction/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "userId":"user-123",
    "amount":500,
    "currency":"USD",
    "merchant":"Amazon",
    "description":"Online purchase"
  }'

# Evaluate Fraud
curl -X POST http://localhost:8080/fraud/evaluate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "transactionId":"txn-123",
    "userId":"user-123",
    "amount":500,
    "merchant":"Amazon"
  }'
```

## Troubleshooting

### Port Already in Use
If a port is already in use:
```bash
# Find process using port (on Windows)
netstat -ano | findstr :8080

# Kill process
taskkill /PID <PID> /F
```

### Maven Build Fails
```bash
# Clean cache and rebuild
mvn clean install -U -DskipTests

# Skip tests if needed
mvn spring-boot:run -DskipTests=true
```

### CORS Issues
CORS is configured to allow `http://localhost:3000`. If you still get CORS errors:
1. Clear browser cache
2. Restart all backend services
3. Check browser console for detailed error

### Database Connection Issues
- Check if H2 console is accessible: `http://localhost:8080/h2-console`
- Default H2 credentials:
  - JDBC URL: `jdbc:h2:mem:testdb`
  - User: `sa`
  - Password: (blank)

### Frontend Won't Load
```bash
cd frontend
rm -r node_modules package-lock.json
npm install
npm start
```

## Environment Variables

### Frontend
Create `.env` in `frontend/` directory:
```
REACT_APP_API_URL=http://localhost:8080
```

### Backend
Services use `application.yml` for configuration. Key properties:
```yaml
server.port: 8080  # Change port if needed
spring.h2.console.enabled: true  # Enable H2 console
```

## Performance Notes

- Initial startup may take 1-2 minutes as all services initialize
- H2 in-memory database resets on service restart
- Use PostgreSQL for persistent data across restarts

## Additional Commands

### View Logs
```bash
# Clear previous logs
mvn clean

# Run with debug logging
export LOGGING_LEVEL_ROOT=DEBUG
mvn spring-boot:run
```

### Database Console (H2)
When using H2, access console at:
```
http://localhost:8080/h2-console
```

### Frontend Build
```bash
cd frontend
npm run build  # Create production build
```

## Next Steps

1. ✅ All services should be running
2. ✅ Navigate to http://localhost:3000
3. ✅ Login with test credentials
4. ✅ Submit transactions to test fraud detection
5. ✅ Check fraud evaluation results

## Support

For issues or questions, refer to:
- [README.md](README.md) - Project overview
- [DESIGN_DOCUMENT.md](DESIGN_DOCUMENT.md) - Architecture details
- [DEV_READINESS_REPORT.md](DEV_READINESS_REPORT.md) - Development status

---

**Last Updated**: May 31, 2026
