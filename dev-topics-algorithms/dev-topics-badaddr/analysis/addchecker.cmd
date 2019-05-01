@echo off
echo. Running Java Bad Adder Explorer test
set starttm=%time%
timer
java -cp . -jar ..\deploy\BadAdder.jar
timer /s
set endtm=%time%
echo.
echo. Done: %starttm% to %endtm%
echo.