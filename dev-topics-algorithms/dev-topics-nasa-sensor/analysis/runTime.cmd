::
: runTime.cmd
:
: Create timming for different painting algorithms
:
@echo off
@echo. -----------------------
@echo. --- Create timmings ---
@echo. -----------------------
setlocal
set classname=demo.algo.sensor.test.TimeExposures
set classpath=..\target\classes;..\target\test-classes
set javacmd=java -cp .;%classpath% %classname% -Xms128M -Xmx512M
echo. $ %javacmd%
%javacmd%
:finis
endlocal
