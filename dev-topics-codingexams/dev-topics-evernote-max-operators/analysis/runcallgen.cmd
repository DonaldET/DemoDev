::
: runcallgen.cmd
::
@echo off
echo. Call Data Generator
setlocal
set classname=don.demo.en.performance.CallGenerator
set classpath=..\target\classes
set javacmd=java -cp .;%classpath% %classname%
echo. $ %javacmd%
%javacmd%
:finis
endlocal