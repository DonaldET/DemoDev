:: run_odo_c.cmd
@echo off
echo. Run the Java odometer version with C optimizations
set xcmd=java -cp ..\target\classes\ don.demodev.kmer.KmerC
echo. $ %xcmd%
%xcmd%
if ERRORLEVEL 1 goto :failed
goto fini
:failed
echo. ERROR - test run failed!
:fini
echo.