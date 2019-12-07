::
: launchIndexRunner.cmd - 2014.8.8
:
: Runs the Jar with the timming tests
::
@echo off
echo.
echo. Running the name index searcher timing test
echo.

setlocal
set xx_jar=target\demodev-topics-simple-linearsearch-1.0.0-SNAPSHOT.jar
set xx_class=demo.don.searcher.runner.IndexRunner
echo.  -- using jar %xx_jar" with class %xx_class%
set xx_cmd=
set xx_cmd=%xx_cmd%%JAVA_HOME%\bin\java
set xx_cmd=%xx_cmd% -jar %xx_jar%
:set xx_cmd=%xx_cmd% %xx_class%
echo. $ %xx_cmd%
%xx_cmd%
if ERRORLEVEL 1 goto error
echo.
goto finis

:
:error
echo.
echo. test failed, errorlevel=%ERRORLEVEL%
goto finis

:
:finis
set xx_emd=
endlocal

:
:Done
echo.
echo. run done.

::
: end launchIndexRunner.cmd
::