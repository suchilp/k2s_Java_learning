@echo off
REM =============================================
REM Setup Java and Maven Environment
REM =============================================

echo Setting up Java and Maven paths...

REM Set JAVA_HOME
setx JAVA_HOME "C:\java\jdk-19.0.2"
echo ✓ JAVA_HOME set

REM Set MAVEN_HOME
setx MAVEN_HOME "C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16"
echo ✓ MAVEN_HOME set

REM Add to PATH (User)
setx Path "%Path%;C:\java\jdk-19.0.2\bin;C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin"
echo ✓ PATH updated

echo.
echo =============================================
echo Setup Complete!
echo =============================================
echo.
echo Please CLOSE and REOPEN PowerShell/Command Prompt
echo Then run:
echo   java -version
echo   mvn -version
echo.
pause
