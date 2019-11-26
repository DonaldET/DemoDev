::
: findPlugins.cmd
:
: Locates plugin entries in nested pom.xml files below the base project directory. Notes:
: o Uses relative addressing to find maven project root (e.g.,
: \GitHub\DemoDev\StaticResources\helpers\src\unittest\resources\..\..\..\..\..)
: o perform a maven clean prior to running
::
@echo off
setlocal
set proj=..\..\..\..\..
set scriptDir=xxx
for %%I in (%0) do set scriptDir=%%~dpI
set baseDir=%scriptDir%%proj%
echo. Capturing tokens in pom.xml files below %baseDir%
echo.

pushd %baseDir%
findstr /S /C:"<artifactId>maven-" pom.xml
popd
goto finis

:finis
echo.
echo. Captured tokens in pom.xml below %baseDir%
endlocal
