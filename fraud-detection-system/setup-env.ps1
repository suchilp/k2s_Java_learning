# =============================================
# Setup Java and Maven Environment Permanently
# =============================================

Write-Host "Setting up Java and Maven paths permanently..." -ForegroundColor Cyan

# Set JAVA_HOME
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\java\jdk-19.0.2", "User")
Write-Host "✓ JAVA_HOME set to: C:\java\jdk-19.0.2" -ForegroundColor Green

# Set MAVEN_HOME
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16", "User")
Write-Host "✓ MAVEN_HOME set to: C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16" -ForegroundColor Green

# Add to PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$newPath = "$currentPath;C:\java\jdk-19.0.2\bin;C:\maven\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin"
[Environment]::SetEnvironmentVariable("Path", $newPath, "User")
Write-Host "✓ PATH updated with Java and Maven bins" -ForegroundColor Green

Write-Host "`n=============================================" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "`nPlease:" -ForegroundColor Yellow
Write-Host "  1. CLOSE all PowerShell windows completely" -ForegroundColor White
Write-Host "  2. OPEN a NEW PowerShell window" -ForegroundColor White
Write-Host "  3. Test with: java -version" -ForegroundColor White
Write-Host "  4. Then run: mvn clean package -DskipTests" -ForegroundColor White
Write-Host "`nPress any key to exit..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
