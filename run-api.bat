@echo off
echo ========================================
echo Smart Garden Simulation API - Runner
echo ========================================
echo.
echo NOTE: The API runs in CONSOLE-ONLY mode (no UI window).
echo       If you want to see the garden visually, run run-ui.bat instead.
echo.
echo Choose an option:
echo   1. Run API (console only, no UI)
echo   2. Run UI (visual garden simulation)
echo   3. Cancel
echo.
set /p choice="Enter choice (1-3): "

if "%choice%"=="1" goto run_api
if "%choice%"=="2" goto run_ui
if "%choice%"=="3" goto end
goto invalid

:run_api
echo.
echo ========================================
echo Running API (Console Only Mode)
echo ========================================
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
echo Build successful! Running API Example...
echo ========================================
echo.
echo The API will:
echo   1. Initialize a garden with plants
echo   2. Display plant information
echo   3. Simulate rainfall
echo   4. Change temperature
echo   5. Trigger pest attacks
echo   6. Get garden state
echo.
echo All events will be logged to log.txt
echo.
echo ========================================
echo.

REM Run the API example using Maven exec plugin
call mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.GardenSimulationAPIExample" -Dexec.classpathScope=compile

echo.
echo ========================================
echo API execution complete!
echo ========================================
echo.
echo Check log.txt for detailed event logs.
echo.
goto end

:run_ui
echo.
echo ========================================
echo Running UI (Visual Garden Simulation)
echo ========================================
echo.
echo Launching the Smart Garden UI...
echo You can manually use the UI to plant seeds and interact with the garden.
echo.
call run.bat
goto end

:invalid
echo Invalid choice. Please enter 1, 2, or 3.
pause
goto end

:end
pause

