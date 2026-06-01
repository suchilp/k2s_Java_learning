#!/usr/bin/env pwsh

# ============================================
# Fraud Detection System - Complete Setup
# ============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Fraud Detection System Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Set paths
$JAVA_HOME = "C:\java\jdk-19_windows-x64_bin\jdk-19.0.1"
$MAVEN_HOME = "C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16"
$PROJECT_ROOT = "c:\k2s\k2s_Java_learning\fraud-detection-system"

Write-Host "`n1. Setting environment variables..." -ForegroundColor Yellow
$env:JAVA_HOME = $JAVA_HOME
$env:MAVEN_HOME = $MAVEN_HOME
$env:Path = "$JAVA_HOME\bin;$MAVEN_HOME\bin;$env:Path"

Write-Host "   JAVA_HOME = $JAVA_HOME" -ForegroundColor Gray
Write-Host "   MAVEN_HOME = $MAVEN_HOME" -ForegroundColor Gray

Write-Host "`n2. Verifying Java..." -ForegroundColor Yellow
$javaExe = "$JAVA_HOME\bin\java.exe"

if (-not (Test-Path $javaExe)) {
    Write-Host "   ❌ Java not found at: $javaExe" -ForegroundColor Red
    Write-Host "   Download Java 17+ from: https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Yellow
    pause
    exit 1
}

Write-Host "   ✅ Java found at: $javaExe" -ForegroundColor Green

Write-Host "`n3. Verifying Maven..." -ForegroundColor Yellow
$mvnExe = "$MAVEN_HOME\bin\mvn.cmd"

if (-not (Test-Path $mvnExe)) {
    Write-Host "   ❌ Maven not found at: $mvnExe" -ForegroundColor Red
    pause
    exit 1
}

Write-Host "   ✅ Maven found at: $mvnExe" -ForegroundColor Green

Write-Host "`n4. Checking Java installation..." -ForegroundColor Yellow
$jvmCfgPath = "$JAVA_HOME\lib\jvm.cfg"

if (-not (Test-Path $jvmCfgPath)) {
    Write-Host "   ⚠️  Warning: jvm.cfg not found" -ForegroundColor Yellow
    Write-Host "   This might cause issues. Consider reinstalling Java." -ForegroundColor Yellow
    Write-Host "   Download from: https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Yellow
}

Write-Host "`n5. Navigating to project..." -ForegroundColor Yellow
Set-Location $PROJECT_ROOT
Write-Host "   ✅ In: $(Get-Location)" -ForegroundColor Green

Write-Host "`n6. Building all services..." -ForegroundColor Yellow
Write-Host "   This may take 5-10 minutes on first build..." -ForegroundColor Gray

& $mvnExe clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "`n❌ Build failed!" -ForegroundColor Red
    Write-Host "`nPossible issues:" -ForegroundColor Yellow
    Write-Host "  1. Java is corrupted (missing jvm.cfg)" -ForegroundColor Gray
    Write-Host "     Fix: Download and install Java 17+ fresh" -ForegroundColor Gray
    Write-Host "  2. Maven download failed" -ForegroundColor Gray
    Write-Host "     Fix: Try: mvn clean -U" -ForegroundColor Gray
    pause
    exit 1
}

Write-Host "`n✅ Build completed successfully!" -ForegroundColor Green

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Next Steps" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host "`nOpen 7 separate PowerShell terminals and run:" -ForegroundColor Yellow

Write-Host "`n📌 Terminal 1 - API Gateway (8080):" -ForegroundColor Cyan
Write-Host "   cd `"$PROJECT_ROOT\api-gateway`"" -ForegroundColor White
Write-Host "   & `"$JAVA_HOME\bin\java.exe`" -jar target/api-gateway-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`n📌 Terminal 2 - Auth Service (8081):" -ForegroundColor Cyan
Write-Host "   cd `"$PROJECT_ROOT\auth-service`"" -ForegroundColor White
Write-Host "   & `"$JAVA_HOME\bin\java.exe`" -jar target/auth-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`n📌 Terminal 3 - Transaction Service (8082):" -ForegroundColor Cyan
Write-Host "   cd `"$PROJECT_ROOT\transaction-service`"" -ForegroundColor White
Write-Host "   & `"$JAVA_HOME\bin\java.exe`" -jar target/transaction-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`n📌 Terminal 4 - Fraud Engine (8083):" -ForegroundColor Cyan
Write-Host "   cd `"$PROJECT_ROOT\fraud-engine`"" -ForegroundColor White
Write-Host "   & `"$JAVA_HOME\bin\java.exe`" -jar target/fraud-engine-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`n📌 Terminal 5 - Notification Service (8084):" -ForegroundColor Cyan
Write-Host "   cd `"$PROJECT_ROOT\notification-service`"" -ForegroundColor White
Write-Host "   & `"$JAVA_HOME\bin\java.exe`" -jar target/notification-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`n📌 Terminal 6 - User Service (8085):" -ForegroundColor Cyan
Write-Host "   cd `"$PROJECT_ROOT\user-service`"" -ForegroundColor White
Write-Host "   & `"$JAVA_HOME\bin\java.exe`" -jar target/user-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`n📌 Terminal 7 - Audit Service (8086):" -ForegroundColor Cyan
Write-Host "   cd `"$PROJECT_ROOT\audit-service`"" -ForegroundColor White
Write-Host "   & `"$JAVA_HOME\bin\java.exe`" -jar target/audit-service-0.0.1-SNAPSHOT.jar" -ForegroundColor White

Write-Host "`n🌐 API Gateway: http://localhost:8080" -ForegroundColor Green
Write-Host "🎨 Frontend: http://localhost:3000 (if you run npm start in frontend folder)" -ForegroundColor Green

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Build Setup Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Press any key to exit..." -ForegroundColor Gray
pause
