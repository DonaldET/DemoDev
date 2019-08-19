::
: runanalyzer.cmd
::
@echo off
echo. Call Data Analyzer
setlocal
set classname=don.demo.en.performance.CallAnalyzer
set classpath=..\target\classes
set javacmd=java -cp .;%classpath% %classname% -Xms128M -Xmx512M
echo. $ %javacmd%
%javacmd%
:finis
endlocal