@echo off
echo ========================================
echo Garden Simulator - 24 Hour Test Script
echo ========================================
echo.
echo This script simulates the professor's 24-hour monitoring scenario.
echo It will:
echo   - Initialize the garden
echo   - Call API methods (rain, temperature, parasite) every hour for 24 hours
echo   - Each hour represents one simulated day
echo   - After 24 days, display final state
echo.
echo NOTE: For faster testing, 1 second = 1 hour (adjustable in code).
echo       For real 24-hour test, change HOUR_IN_MILLIS to 3600000.
echo.

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH!
    echo.
    echo Please install Java 21:
    echo 1. See INSTALL_JAVA_WINDOWS.md for instructions
    echo 2. Or visit: https://adoptium.net/temurin/releases/?version=21
    echo.
    pause
    exit /b 1
)

echo Java found! Checking version...
java -version
echo.

REM Check if JAVA_HOME is set
if "%JAVA_HOME%"=="" (
    echo WARNING: JAVA_HOME is not set!
    echo Maven wrapper will try to use Java from PATH...
    echo.
)

echo Building project...
echo.
call mvnw.cmd clean compile -DskipTests
if errorlevel 1 (
    echo.
    echo BUILD FAILED!
    echo Check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build successful! Running 24-Hour Test...
echo ========================================
echo.
echo The simulation will run for 24 simulated days.
echo Each day takes 1 second (for testing - adjust in code for real 24 hours).
echo.
echo All events will be logged to log.txt
echo.
echo ========================================
echo.

REM Run the Garden Simulator using Maven exec plugin
call mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulator" -Dexec.classpathScope=compile

echo.
echo ========================================
echo 24-Hour Test Complete!
echo ========================================
echo.
echo Check log.txt for detailed event logs.
echo Review the output above for final garden state.
echo.
pause

