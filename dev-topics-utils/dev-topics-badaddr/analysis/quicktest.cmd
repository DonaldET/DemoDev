@echo off
echo Running quick addition test
time/T
java -cp .;..\target\classes\ don.demo.datagen.QuickAddChecker
time/T
echo Done