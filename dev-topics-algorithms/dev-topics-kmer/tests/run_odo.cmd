:: run_odo.cmd
@echo off
echo. Run the Java odometer version
set xcmd=java -cp ..\target\classes\ don.demodev.kmer.KmerOdometer
echo. $ %xcmd%
%xcmd%
if ERRORLEVEL 1 goto :failed
goto fini
:failed
echo. ERROR - test run failed!
:fini
echo.