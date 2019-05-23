::
: quickRtest.cmd
::
@echo off
echo. Running R quick addition test
set starttm=%time%
timer
"D:\Program Files\R\R-3.6.0\bin\x64\Rscript.exe" QuickAddCheck.R
timer /s
set endtm=%time%
echo.
echo. Done: %starttm% to %endtm%
echo.