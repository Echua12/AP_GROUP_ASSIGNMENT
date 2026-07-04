@echo off
setlocal enabledelayedexpansion

:: ==========================================
:: CONFIGURATION: Change this to your root project folder
:: ==========================================
set "PROJECT_ROOT=%~dp0campus-mcp-assignment"

echo Navigating to project root folder...
cd /d "%PROJECT_ROOT%"
if %errorLevel% neq 0 (
    echo [ERROR] Could not find the directory: %PROJECT_ROOT%
    pause
    exit /b
)

:: 1. Build the entire project
echo Building project with Maven...
echo ------------------------------------------------
call mvn clean package
if %errorLevel% neq 0 (
    echo [ERROR] Maven build failed! Please check your code.
    pause
    exit /b
)
echo [SUCCESS] Project built successfully.
echo.

:: 2. Launch the Server in a separate window
echo Starting Campus Info MCP Server in a new window...
echo ------------------------------------------------
start "Campus MCP Server" cmd /k "java -jar campus-info-mcp-server/target/campus-info-mcp-server.jar"

:: Give the server a brief 3-second window to start binding to its port
echo Waiting for server to initialize...
timeout /t 3 /nobreak >nul
echo.

:: 3. Navigate to client folder and launch the JavaFX UI
echo Navigating to JavaFX Client directory...
cd .\reference-javafx-client\
if %errorLevel% neq 0 (
    echo [ERROR] Could not find reference-javafx-client directory.
    pause
    exit /b
)

echo Starting JavaFX Client...
echo ------------------------------------------------
call set GEMINI_API_KEY=AQ.Ab8RN6JTMIIX-YMp7So-LgV-lqf12uixNqOZ7pXAxHVmyjz4pg
call mvn compile javafx:run

echo.
echo Application execution finished.
pause