::
: count_built_jars.cmd
:
: Drop through all directories finding jar files, but ingore the 'original' files created by shading
::
@echo off
echo.
setlocal
echo. Counting target Jar files from Project Root
set myDir=xxxx
for %%F in (%0) do set myDir=%%~dpF
echo.                     working from: %myDir%
pushd %myDir%

dir/s/b ..\*SNAPSHOT.jar | find "\target\" | find /v "\lib\" | find /v "original-" | find /v "\example\" | find /c /v ""
if ERRORLEVEL 1 goto dir_failed
goto fini

:
:dir_failed
echo.
echo. ERROR: No log file name given as first parameter
echo.

:
:fini
popd
echo.
echo. Maven built Jar count Done
endlocal
