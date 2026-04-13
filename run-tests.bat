@echo off
REM =============================================================================
REM HMS - Run All Tests (Windows)
REM =============================================================================
REM Usage:
REM   run-tests.bat              Run backend unit tests only
REM   run-tests.bat --all        Run backend unit + Selenium UI tests
REM   run-tests.bat --selenium   Run Selenium UI tests only
REM   run-tests.bat --help       Show usage
REM
REM Prerequisites:
REM   Backend unit tests: Java 21+, Gradle (no external services needed)
REM   Selenium tests:     Chrome browser, backend on :8080, frontend on :3000
REM =============================================================================

setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
set BACKEND_DIR=%SCRIPT_DIR%backend

if "%~1"=="--help" goto :show_help
if "%~1"=="--all" goto :run_all
if "%~1"=="--selenium" goto :run_selenium
goto :run_backend

:run_all
call :run_backend_tests
if errorlevel 1 exit /b 1
call :run_selenium_tests
if errorlevel 1 exit /b 1
echo.
echo =============================================
echo   All Tests Complete
echo =============================================
echo All tests passed successfully!
goto :eof

:run_backend
call :run_backend_tests
goto :eof

:run_selenium
call :run_selenium_tests
goto :eof

:run_backend_tests
echo.
echo =============================================
echo   Running Backend Unit Tests (JUnit 5)
echo =============================================
echo.
cd /d "%BACKEND_DIR%"
if exist "gradlew.bat" (
    call gradlew.bat test --no-daemon
) else (
    gradle test --no-daemon
)
if errorlevel 1 (
    echo [FAIL] Backend unit tests FAILED
    exit /b 1
)
echo [PASS] Backend unit tests PASSED
exit /b 0

:run_selenium_tests
echo.
echo =============================================
echo   Running Selenium UI Tests
echo =============================================
echo.
echo Checking prerequisites...
curl -s http://localhost:8080/api/auth/login >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Backend is not running on port 8080
    echo Start the backend first: cd backend ^&^& gradlew bootRun
    exit /b 1
)
curl -s http://localhost:3000 >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Frontend is not running on port 3000
    echo Start the frontend first: cd frontend ^&^& npm run dev
    exit /b 1
)
echo Backend and frontend are running. Starting Selenium tests...
cd /d "%BACKEND_DIR%"
if exist "gradlew.bat" (
    call gradlew.bat seleniumTest --no-daemon
) else (
    gradle seleniumTest --no-daemon
)
if errorlevel 1 (
    echo [FAIL] Selenium tests FAILED
    exit /b 1
)
echo [PASS] Selenium tests PASSED
exit /b 0

:show_help
echo HMS Test Runner
echo.
echo Usage: run-tests.bat [OPTION]
echo.
echo Options:
echo   (none)       Run backend unit tests only
echo   --all        Run backend unit tests + Selenium UI tests
echo   --selenium   Run Selenium UI tests only
echo   --help       Show this help message
echo.
echo Backend tests use H2 in-memory database (no MySQL required).
echo Selenium tests require backend (:8080) and frontend (:3000) running.
goto :eof
