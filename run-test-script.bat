@echo off
echo ========================================
echo Garden Simulation API - Test Script
echo ========================================
echo.
echo This script runs a simple test of all API methods.
echo Matches the specification example pattern.
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

echo Java found! Building project...
echo.

REM Build the project
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
echo Build successful! Running Test Script...
echo ========================================
echo.
echo The test will:
echo   - Initialize garden from config
echo   - Test rain(), temperature(), and parasite() methods
echo   - Show final garden state
echo.
echo All events will be logged to log.txt
echo.
echo ========================================
echo.

REM Run the test script
call mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.TestScript" -Dexec.classpathScope=compile

echo.
echo ========================================
echo Test Complete!
echo ========================================
echo.
echo Check log.txt for detailed event logs.
echo Location: D:\smartGarden\log.txt
echo.
pause

