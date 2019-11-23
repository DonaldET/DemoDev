::
: findPlugins.cmd
:
: Locates plugin entries in nested pom.xml files. Note: perform a maven clean prior to running
::
@echo off
setlocal
set proj=..\..\..\..\..
echo. Capturing tokens in pom.xml files below %proj%
echo.

pushd %proj%
findstr /S /C:"<artifactId>maven-" pom.xml
popd
goto finis



:finis
echo.
echo. Captured tokens in pom.xml below %proj%
endlocal
