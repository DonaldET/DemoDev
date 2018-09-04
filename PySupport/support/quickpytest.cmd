@echo off
echo. Running quick addition test
set starttm=%time%
python add_tester.py
set endtm=%time%
echo. Done: %starttm% to %endtm%