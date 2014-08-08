::
: launchIndexRunner.cmd - 2014.8.8
:
: Runs the Jar with the timming tests
::
@echo off
echo.
echo. Running the name index searcher timming test
echo.

setlocal
set xx_cmd=
set xx_cmd=%xx_cmd%%JAVA_HOME%\bin\java
set xx_cmd=%xx_cmd% -cp .;
set xx_cmd=%xx_cmd%.\commons-lang3-3.1.jar;
set xx_cmd=%xx_cmd%.\target\simple-linearsearch-1.0.0-SNAPSHOT.jar
set xx_cmd=%xx_cmd% demo.don.searcher.runner.IndexRunner
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