@echo off
echo. Running quick Java addition test
set starttm=%time%
java -cp .;..\target\classes\ don.demo.datagen.QuickAddChecker
set endtm=%time%
echo. Done: %starttm% to %endtm%