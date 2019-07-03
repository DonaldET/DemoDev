::::
: run_perform.cmd
:
: Execute a performance run to test BitSearcher; ordered vs unordered search
::::
@echo off
echo.
echo. Execute Performance Run to test BitSearcher; ordered vs unordered search
echo.
setlocal
set path2classes=..\target\classes
java -cp .;%path2classes% demo.liveramp.bitsearch.performance.PerformanceRunner
if ERRORLEVEL 1 goto error
goto finis
:
:error
echo.
echo. Performance run failed!
echo.
endlocal
:
:finis
echo.
echo. Done
echo.