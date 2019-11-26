::
: get_pom_tokens.cmd
:
: Locates maven plugin entries in nested pom.xml files defined by the script findPlugins.cmd.
: Note: run mvn clean prior to scan.
::
@echo off
setlocal
set lst=maven-plugins.txt
set finder=..\helpers\src\unittest\resources
echo.
echo. Listing MAVEN plugins in POM files into file %lst% using %finder%
echo. Execute maven clean first!
call %finder%\findPlugins.cmd > %lst%
echo. Done with %lst%
endlocal

