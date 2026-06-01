@echo off
REM Set JAVA_HOME environment variable
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo JAVA_HOME is set to: %JAVA_HOME%
echo Java version:
java -version

echo.
echo Starting Auth Service on port 8081...
echo.

cd /d C:\k2s\k2s_Java_learning\fraud-detection-system\auth-service
mvn spring-boot:run
