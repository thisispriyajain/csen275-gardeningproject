@echo off
echo ========================================
echo Quick API Test - All Methods
echo ========================================
echo.
echo This script quickly tests all API methods:
echo   - Rain (sprinklers should stop)
echo   - Temperature (heating should activate/deactivate)
echo   - Parasite (pesticide should apply automatically)
echo.
echo Takes about 15 seconds to complete.
echo.

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH!
    pause
    exit /b 1
)

echo Building project...
call mvnw.cmd clean compile -DskipTests
if errorlevel 1 (
    echo BUILD FAILED!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Running Quick Test...
echo ========================================
echo.

call mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.QuickAPITest" -Dexec.classpathScope=compile

echo.
echo ========================================
echo Test Complete!
echo ========================================
echo.
echo Check log.txt for detailed logs.
pause

