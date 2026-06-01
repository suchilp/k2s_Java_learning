#!/usr/bin/env pwsh

# Set Maven path
$env:MAVEN_HOME = "C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16"
$env:Path = "$env:MAVEN_HOME\bin;$env:Path"

# Navigate to project root
Set-Location "c:\k2s\k2s_Java_learning\fraud-detection-system"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Building Fraud Detection System..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Build all services
& "$env:MAVEN_HOME\bin\mvn.cmd" clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "`n❌ Build failed!" -ForegroundColor Red
    Write-Host "Make sure you have Java 17+ installed properly." -ForegroundColor Red
    pause
    exit 1
}

Write-Host "`n✅ Build completed successfully!" -ForegroundColor Green
Write-Host "`nNext steps: Open 7 PowerShell terminals and run these commands:" -ForegroundColor Yellow
Write-Host "`nTerminal 1 - API Gateway (Port 8080):" -ForegroundColor Cyan
Write-Host "  cd api-gateway; java -jar target/api-gateway-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`nTerminal 2 - Auth Service (Port 8081):" -ForegroundColor Cyan
Write-Host "  cd auth-service; java -jar target/auth-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`nTerminal 3 - Transaction Service (Port 8082):" -ForegroundColor Cyan
Write-Host "  cd transaction-service; java -jar target/transaction-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`nTerminal 4 - Fraud Engine (Port 8083):" -ForegroundColor Cyan
Write-Host "  cd fraud-engine; java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`nTerminal 5 - Notification Service (Port 8084):" -ForegroundColor Cyan
Write-Host "  cd notification-service; java -jar target/notification-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`nTerminal 6 - User Service (Port 8085):" -ForegroundColor Cyan
Write-Host "  cd user-service; java -jar target/user-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`nTerminal 7 - Audit Service (Port 8086):" -ForegroundColor Cyan
Write-Host "  cd audit-service; java -jar target/audit-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`nAPI Gateway: http://localhost:8080" -ForegroundColor Green
