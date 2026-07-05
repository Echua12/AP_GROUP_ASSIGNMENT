@echo off
:: Ensure the script is running with Administrator privileges echo
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo CRITICAL ERROR: This script must be run as an Administrator!
    echo Right-click the .bat file and select "Run as administrator".
    pause
    exit /b
)

:: CREATING THE BIN PATH PATH

:GET_BIN
set /p "BIN_PATH=Enter the path for the BIN folder (tip: copy and paste the path URL from File Explorer) >> "
:: Optional check: Ensure the user didn't leave it blank
if "%BIN_PATH%"=="" (
    echo Error: Path cannot be empty. Please try again.
    goto :GET_BIN
)

:: 2. Append the new folder to the System PATH
echo Appending BIN_PATH to System PATH Variable...
:: We use a temporary PowerShell command to cleanly append to the machine PATH without hitting the 1024 character limit of setx
powershell -Command "[Environment]::SetEnvironmentVariable('Path', [Environment]::GetEnvironmentVariable('Path', 'Machine') + ';%BIN_PATH%', 'Machine')"

if not errorLevel 1 (
    echo [SUCCESS] Added "%BIN_PATH%" to the System PATH Variable.
) else (
    echo [ERROR] Failed to update System PATH Variable, please check your path URL!
    goto :GET_BIN
)

:: CREATING THE MAVEN_HOME VAR


:GET_MAVEN
set /p "MAVEN_PATH=Enter the path for the MAVEN folder (tip: copy and paste the path URL from File Explorer) >> "
if "%MAVEN_PATH%"=="" (
    echo Error: Path cannot be empty. Please try again.
    goto :GET_MAVEN
)

:: 1. Create the System Variable "TEST"
echo Creating New System Variable: MAVEN_HOME...
setx MAVEN_HOME "%MAVEN_PATH%" /M
if not errorLevel 1 (
    echo [SUCCESS] MAVEN_HOME variable set to "%MAVEN_PATH%"
) else (
    echo [ERROR] Failed to set MAVEN_HOME variable, please check your path URL!
    goto :GET_MAVEN
)

echo Done! Note: Make sure to verify the changes on your own
pause

