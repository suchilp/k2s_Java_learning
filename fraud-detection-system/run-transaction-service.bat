@echo off
REM Set JAVA_HOME environment variable
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

cd /d C:\k2s\k2s_Java_learning\fraud-detection-system\transaction-service
mvn spring-boot:run
