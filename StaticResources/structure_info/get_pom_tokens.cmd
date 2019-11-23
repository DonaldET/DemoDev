@echo off
setlocal
set lst=maven-plugins.txt
echo.
echo. Listing MAVEN plugins in POM files into file %lst%
echo. Execute maven clean first!
pushd ..\helpers\src\unittest\resources\
call findPlugins.cmd > %lst%

echo. Done with %lst%
endlocal

