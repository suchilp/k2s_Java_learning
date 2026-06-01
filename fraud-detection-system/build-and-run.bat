@echo off
REM Set Maven path
set MAVEN_HOME=C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16
set PATH=%MAVEN_HOME%\bin;%PATH%

REM Navigate to project root
cd /d c:\k2s\k2s_Java_learning\fraud-detection-system

REM Build all services
echo Building all services...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Check your Java installation.
    echo Make sure you have Java 17+ installed properly.
    pause
    exit /b 1
)

echo.
echo Build completed successfully!
echo.
echo Next steps: Open 7 PowerShell terminals and run these commands:
echo.
echo Terminal 1 - API Gateway:
echo   cd api-gateway ^&^& java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
echo.
echo Terminal 2 - Auth Service:
echo   cd auth-service ^&^& java -jar target/auth-service-0.0.1-SNAPSHOT.jar
echo.
echo Terminal 3 - Transaction Service:
echo   cd transaction-service ^&^& java -jar target/transaction-service-0.0.1-SNAPSHOT.jar
echo.
echo Terminal 4 - Fraud Engine:
echo   cd fraud-engine ^&^& java -jar target/fraud-engine-0.0.1-SNAPSHOT.jar
echo.
echo Terminal 5 - Notification Service:
echo   cd notification-service ^&^& java -jar target/notification-service-0.0.1-SNAPSHOT.jar
echo.
echo Terminal 6 - User Service:
echo   cd user-service ^&^& java -jar target/user-service-0.0.1-SNAPSHOT.jar
echo.
echo Terminal 7 - Audit Service:
echo   cd audit-service ^&^& java -jar target/audit-service-0.0.1-SNAPSHOT.jar
echo.
echo Gateway: http://localhost:8080
pause
