# Setup Java and Maven Environment Permanently

Write-Host "Setting up Java and Maven paths permanently..." -ForegroundColor Cyan

# Set JAVA_HOME
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\java\jdk-19.0.2", "User")
Write-Host "OK: JAVA_HOME set" -ForegroundColor Green

# Set MAVEN_HOME
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16", "User")
Write-Host "OK: MAVEN_HOME set" -ForegroundColor Green

# Add to PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$newPath = "$currentPath;C:\java\jdk-19.0.2\bin;C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin"
[Environment]::SetEnvironmentVariable("Path", $newPath, "User")
Write-Host "OK: PATH updated" -ForegroundColor Green

Write-Host "`nSetup Complete!" -ForegroundColor Yellow
Write-Host "Please close and reopen PowerShell, then run: java -version" -ForegroundColor White
