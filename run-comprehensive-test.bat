@echo off
echo ========================================
echo Comprehensive API Test - All Scenarios
echo ========================================
echo.
echo This test covers:
echo   - Water decreasing naturally
echo   - Sprinkler activation when water is low
echo   - Sprinkler deactivation when raining
echo   - Pest attacks and pesticide application
echo   - Heating activation/deactivation
echo   - Water stress damage
echo   - All automatic system responses
echo.
echo Takes approximately 30 seconds to complete.
echo.
pause

call mvnw.cmd clean compile -DskipTests
if errorlevel 1 (
    echo.
    echo BUILD FAILED!
    pause
    exit /b 1
)

echo.
echo Build successful! Starting Comprehensive Test...
echo.

call mvnw.cmd exec:java -Dexec.mainClass="edu.scu.csen275.smartgarden.api.ComprehensiveAPITest" -Dexec.classpathScope=compile

echo.
echo Comprehensive Test complete!
echo Check log.txt for detailed system responses.
echo.
pause

