@echo off
setlocal

set "ADB_EXE="
if defined ANDROID_SDK_ROOT if exist "%ANDROID_SDK_ROOT%\platform-tools\adb.exe" set "ADB_EXE=%ANDROID_SDK_ROOT%\platform-tools\adb.exe"
if not defined ADB_EXE if defined ANDROID_HOME if exist "%ANDROID_HOME%\platform-tools\adb.exe" set "ADB_EXE=%ANDROID_HOME%\platform-tools\adb.exe"
if not defined ADB_EXE for %%A in (adb.exe) do if not "%%~$PATH:A"=="" set "ADB_EXE=%%~$PATH:A"

set "ADB_TARGET=-d"
if not "%~1"=="" set "ADB_TARGET=-s %~1"
if defined ADB_EXE "%ADB_EXE%" %ADB_TARGET% reverse --remove tcp:5556 >nul 2>&1

taskkill /IM Simulator.exe /T >nul 2>&1
echo Simple EMV simulator stopped and adb reverse tcp:5556 removed.
exit /b 0
