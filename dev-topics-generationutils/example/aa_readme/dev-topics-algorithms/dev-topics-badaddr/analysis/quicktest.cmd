@echo off
echo. Running Java Quick Addition Checker
set starttm=%time%
timer
java -cp .;..\target\classes\ don.demo.datagen.QuickAddChecker
timer /s
set endtm=%time%
echo.
echo. Done: %starttm% to %endtm%
echo.