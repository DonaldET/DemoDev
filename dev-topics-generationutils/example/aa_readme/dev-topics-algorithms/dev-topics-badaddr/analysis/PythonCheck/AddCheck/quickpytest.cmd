@echo off
echo. Running Python quick addition test
set starttm=%time%
timer
python add_tester.py
timer /s
set endtm=%time%
echo.
echo. Done: %starttm% to %endtm%
echo.