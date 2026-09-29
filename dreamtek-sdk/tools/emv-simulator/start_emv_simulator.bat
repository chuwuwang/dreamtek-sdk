@echo off
setlocal
pushd "%~dp0"

set "ADB_EXE="
if defined ANDROID_SDK_ROOT if exist "%ANDROID_SDK_ROOT%\platform-tools\adb.exe" set "ADB_EXE=%ANDROID_SDK_ROOT%\platform-tools\adb.exe"
if not defined ADB_EXE if defined ANDROID_HOME if exist "%ANDROID_HOME%\platform-tools\adb.exe" set "ADB_EXE=%ANDROID_HOME%\platform-tools\adb.exe"
if not defined ADB_EXE for %%A in (adb.exe) do if not "%%~$PATH:A"=="" set "ADB_EXE=%%~$PATH:A"

if not defined ADB_EXE (
    echo ADB was not found. Set ANDROID_SDK_ROOT or add platform-tools to PATH.
    popd
    exit /b 1
)

set "ADB_TARGET=-d"
if not "%~1"=="" set "ADB_TARGET=-s %~1"

"%ADB_EXE%" %ADB_TARGET% get-state 2>nul | findstr /x "device" >nul
if errorlevel 1 (
    echo No online POS device was found. Pass its serial as the first argument if needed.
    popd
    exit /b 2
)

"%ADB_EXE%" %ADB_TARGET% reverse tcp:5556 tcp:5557
if errorlevel 1 (
    echo Failed to create adb reverse tcp:5556 to tcp:5557.
    popd
    exit /b 3
)

echo ADB reverse is ready: App 127.0.0.1:5556 to PC simulator port 5557.
echo In Simulator, set the TCP/IP listening port to 5557 and start listening.
start "Simple EMV Simulator" "%~dp0Simulator.exe"
popd
exit /b 0
