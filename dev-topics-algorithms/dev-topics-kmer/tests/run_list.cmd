:: run_list.cmd
@echo off
echo. Run the Java odometer version with list construction
set xcmd=java -cp ..\target\classes\ don.demodev.kmer.KmerLists
echo. $ %xcmd%
%xcmd%
if ERRORLEVEL 1 goto :failed
goto fini
:failed
echo. ERROR - test run failed!
:fini
echo.