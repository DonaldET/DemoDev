::
: runner.cmd
::
@echo off
echo.
setlocal
set jarpath=target\demodev-topics-gesichtbuch-infrastructure-1.0.0-SNAPSHOT.jar
java -cp .;%jarpath% demo.gbucher.infra_exams.TaskTimer
java -cp .;%jarpath% demo.gbucher.infra_exams.SubStringFinder
:finis
endlocal
echo.
echo. Done