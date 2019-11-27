::
: count_maven_jars.cmd
:
: Examine Maven build long finding jar deployed projects (i.e., [ jar])
::
@echo off
echo.
setlocal
echo. Count JAR deployed projects in Maven build log file
set myDir=xxxx
for %%F in (%0) do set myDir=%%~dpF
echo.                     working from: %myDir%
pushd %myDir%

set logFile=%1
if not "%logFile%" == "" goto check_log
echo.
echo. ERROR: No log file name given as first parameter
echo.
goto fini

:
:check_log
if exist %logFile% goto find_jar_deploys
echo.
echo. ERROR: Log file %logFile does not exist
echo.
goto fini

:
:find_jar_deploys
find "[jar]" %logFile% | find /c /v ""
if ERRORLEVEL 1 goto count_failed
echo.
goto fini

:
:count_failed
echo.
echo. ERROR: Search for Jar deploys in log file failed
echo.
goto fini

:
:fini
popd
echo.
echo. JAR deployment count done
endlocal
