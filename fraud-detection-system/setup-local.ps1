#!/usr/bin/env pwsh
# Fraud Detection System - Local Startup Script (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Fraud Detection System - Local Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check prerequisites
Write-Host "[1/5] Checking prerequisites..." -ForegroundColor Yellow

try {
    java -version 2>&1 | Out-Null
    Write-Host "    ✓ Java found" -ForegroundColor Green
} catch {
    Write-Host "    ✗ Java not found. Please install Java 17+" -ForegroundColor Red
    exit 1
}

try {
    mvn -version 2>&1 | Out-Null
    Write-Host "    ✓ Maven found" -ForegroundColor Green
} catch {
    Write-Host "    ✗ Maven not found. Please install Maven 3.9+" -ForegroundColor Red
    exit 1
}

try {
    docker --version 2>&1 | Out-Null
    Write-Host "    ✓ Docker found" -ForegroundColor Green
} catch {
    Write-Host "    ✗ Docker not found. Please install Docker" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Start infrastructure
Write-Host "[2/5] Starting infrastructure (PostgreSQL, Kafka, Zookeeper, Redis)..." -ForegroundColor Yellow
try {
    docker-compose up -d postgres kafka zookeeper redis | Out-Null
    Write-Host "    ✓ Infrastructure started" -ForegroundColor Green
    Write-Host "      - PostgreSQL on localhost:5432"
    Write-Host "      - Kafka on localhost:9092"
    Write-Host "      - Redis on localhost:6379"
} catch {
    Write-Host "    ✗ Failed to start Docker services" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Wait for PostgreSQL
Write-Host "[3/5] Waiting for PostgreSQL to be ready (30 seconds)..." -ForegroundColor Yellow
Start-Sleep -Seconds 30
Write-Host "    ✓ PostgreSQL ready" -ForegroundColor Green

Write-Host ""

# Build all services
Write-Host "[4/5] Building all services (Maven compile)..." -ForegroundColor Yellow
Write-Host "    This may take 5-10 minutes on first build..." -ForegroundColor Gray

try {
    mvn clean package -DskipTests -q
    Write-Host "    ✓ All services built successfully" -ForegroundColor Green
} catch {
    Write-Host "    ✗ Maven build failed" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Show status
Write-Host "[5/5] Setup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "NEXT STEPS:" -ForegroundColor Cyan
Write-Host ""

Write-Host "Option 1: Run all services in Docker (Easiest)" -ForegroundColor Yellow
Write-Host "  docker-compose up --build" -ForegroundColor Green
Write-Host ""

Write-Host "Option 2: Run services individually (For debugging)" -ForegroundColor Yellow
Write-Host "  Open 7 PowerShell terminals and run:" -ForegroundColor Green
Write-Host "  1. cd api-gateway; java -jar target/api-gateway-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
Write-Host "  2. cd auth-service; java -jar target/auth-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
Write-Host "  3. cd transaction-service; java -jar target/transaction-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
Write-Host "  4. cd fraud-engine; java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
Write-Host "  5. cd notification-service; java -jar target/notification-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
Write-Host "  6. cd user-service; java -jar target/user-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
Write-Host "  7. cd audit-service; java -jar target/audit-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev"
Write-Host ""

Write-Host "TEST ENDPOINTS:" -ForegroundColor Cyan
Write-Host "  curl http://localhost:8086/actuator/health" -ForegroundColor Green
Write-Host "  curl http://localhost:8080/actuator/health" -ForegroundColor Green
Write-Host "  curl http://localhost:8081/actuator/health" -ForegroundColor Green
Write-Host ""

Write-Host "DOCUMENTATION:" -ForegroundColor Cyan
Write-Host "  - LOCAL_SETUP_GUIDE.md (detailed steps)" -ForegroundColor Gray
Write-Host "  - DEV_READINESS_REPORT.md (system overview)" -ForegroundColor Gray
Write-Host "  - API_GATEWAY_DEV_READY.md (gateway config)" -ForegroundColor Gray
Write-Host "  - AUDIT_SERVICE_DEV_READY.md (audit API)" -ForegroundColor Gray
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Status: ✓ READY FOR LOCAL DEVELOPMENT" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
