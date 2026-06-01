@echo off
REM Set JAVA_HOME environment variable
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo JAVA_HOME is set to: %JAVA_HOME%
echo Java version:
java -version

echo.
echo Building all services...
echo.

echo Building Auth Service...
cd /d C:\k2s\k2s_Java_learning\fraud-detection-system\auth-service
mvn clean install -DskipTests -q
if errorlevel 1 goto :error

echo Building Transaction Service...
cd /d C:\k2s\k2s_Java_learning\fraud-detection-system\transaction-service
mvn clean install -DskipTests -q
if errorlevel 1 goto :error

echo Building Fraud Engine...
cd /d C:\k2s\k2s_Java_learning\fraud-detection-system\fraud-engine
mvn clean install -DskipTests -q
if errorlevel 1 goto :error

echo Building Notification Service...
cd /d C:\k2s\k2s_Java_learning\fraud-detection-system\notification-service
mvn clean install -DskipTests -q
if errorlevel 1 goto :error

echo Building User Service...
cd /d C:\k2s\k2s_Java_learning\fraud-detection-system\user-service
mvn clean install -DskipTests -q
if errorlevel 1 goto :error

cd /d C:\k2s\k2s_Java_learning\fraud-detection-system

echo.
echo ========================================
echo All services built successfully!
echo ========================================
echo.
echo To start services, run these commands in separate terminals:
echo.
echo 1. Auth Service (8081):
echo    C:\k2s\k2s_Java_learning\fraud-detection-system\run-auth-service.bat
echo.
echo 2. Transaction Service (8082):
echo    C:\k2s\k2s_Java_learning\fraud-detection-system\run-transaction-service.bat
echo.
echo 3. Fraud Engine (8083):
echo    C:\k2s\k2s_Java_learning\fraud-detection-system\run-fraud-engine.bat
echo.
echo 4. Notification Service (8084):
echo    C:\k2s\k2s_Java_learning\fraud-detection-system\run-notification-service.bat
echo.
echo 5. User Service (8085):
echo    C:\k2s\k2s_Java_learning\fraud-detection-system\run-user-service.bat
echo.
echo 6. Frontend (3000):
echo    cd C:\k2s\k2s_Java_learning\fraud-detection-system\frontend
echo    npm start
echo.
pause
goto :end

:error
echo.
echo ========================================
echo ERROR: Build failed!
echo ========================================
echo.
pause
goto :end

:end
