@echo off
REM Fraud Detection System - Local Startup Script for Windows

echo ========================================
echo Fraud Detection System - Local Setup
echo ========================================
echo.

REM Check prerequisites
echo [1/5] Checking prerequisites...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found. Please install Java 17+
    exit /b 1
)

mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven not found. Please install Maven 3.9+
    exit /b 1
)

docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker not found. Please install Docker
    exit /b 1
)

echo [✓] All prerequisites found
echo.

REM Start infrastructure
echo [2/5] Starting infrastructure (PostgreSQL, Kafka, Zookeeper, Redis)...
docker-compose up -d postgres kafka zookeeper redis
if errorlevel 1 (
    echo ERROR: Failed to start Docker services
    exit /b 1
)

echo [✓] Infrastructure started
echo     - PostgreSQL on localhost:5432
echo     - Kafka on localhost:9092
echo     - Redis on localhost:6379
echo.

REM Wait for PostgreSQL
echo [3/5] Waiting for PostgreSQL to be ready (30 seconds)...
timeout /t 30 /nobreak

REM Build all services
echo.
echo [4/5] Building all services (Maven compile)...
echo     This may take 5-10 minutes on first build...
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERROR: Maven build failed
    exit /b 1
)
echo [✓] All services built successfully
echo.

REM Show status
echo [5/5] Status Check
echo ========================================
echo All services are ready to run!
echo.

echo Option 1: Run all services in Docker
echo     Command: docker-compose up --build
echo.

echo Option 2: Run services individually in separate PowerShell terminals
echo     Terminal 1: cd api-gateway ^&^& java -jar target/api-gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
echo     Terminal 2: cd auth-service ^&^& java -jar target/auth-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
echo     Terminal 3: cd transaction-service ^&^& java -jar target/transaction-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
echo     Terminal 4: cd fraud-engine ^&^& java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
echo     Terminal 5: cd notification-service ^&^& java -jar target/notification-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
echo     Terminal 6: cd user-service ^&^& java -jar target/user-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
echo     Terminal 7: cd audit-service ^&^& java -jar target/audit-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
echo.

echo Test endpoints (use in PowerShell):
echo     curl http://localhost:8086/actuator/health
echo     curl http://localhost:8080/actuator/health
echo.

echo Documentation:
echo     - LOCAL_SETUP_GUIDE.md (detailed setup steps)
echo     - DEV_READINESS_REPORT.md (complete system overview)
echo     - API_GATEWAY_DEV_READY.md (gateway configuration)
echo     - AUDIT_SERVICE_DEV_READY.md (audit service API)
echo.

echo ========================================
pause
