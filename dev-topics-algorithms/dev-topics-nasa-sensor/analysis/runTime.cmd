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
set classname=don.demo.ww2.fjbarn.test.TimePainting
set classpath=..\bin
set javacmd=java -cp .;%classpath% %classname% -Xms128M -Xmx512M
echo. $ %javacmd%
%javacmd%
:finis
endlocal
