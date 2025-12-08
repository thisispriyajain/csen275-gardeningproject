@echo off
echo ========================================
echo Smart Garden Simulation - UI with API Mode
echo ========================================
echo.
echo This will launch the UI with API mode enabled.
echo The API will use the same garden state as the UI.
echo.
echo You can then call API methods from another terminal/script,
echo and the UI will update automatically via EventBus.
echo.
echo To run API calls from another terminal:
echo   run-api.bat (option 1) - API only mode
echo   OR use QuickAPITest: run-quick-test.bat
echo.
echo NOTE: When API mode is enabled:
echo   - Automatic pest spawning is DISABLED (only via API)
echo   - Automatic weather changes are DISABLED (only via API)
echo   - All other systems work automatically (pesticide, heating, sprinklers)
echo.
pause

echo.
echo Building project...
echo.
call mvnw.cmd clean compile -DskipTests
if errorlevel 1 (
    echo.
    echo BUILD FAILED!
    pause
    exit /b 1
)

echo.
echo Launching UI with API mode enabled...
echo.

REM Run UI with API mode enabled via system property
call mvnw.cmd javafx:run -Dsmartgarden.api.enabled=true

pause

