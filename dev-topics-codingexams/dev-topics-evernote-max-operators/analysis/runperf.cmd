::
: runperf.cmd
::
@echo off
echo. Performance Analyzer
setlocal
set classname=don.demo.en.performance.PerformanceRunner
set classpath=..\target\classes
set javacmd=java -cp .;%classpath% %classname% -Xms128M -Xmx512M
echo. $ %javacmd%
%javacmd%
:finis
endlocal