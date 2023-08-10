:: submit_smoke.cmd
: Examples: https://sparkbyexamples.com/spark/spark-submit-command/
::

@echo off
setlocal
set x_script=$0
set x_driver=smoketest.py

echo.
echo. Summit Spark Smoke Test $x_driver% on %SPARK_HOME% using %x_script%.cmd
set x_base=%~dp0
echo.   -- Base Path  : %x_base%
set x_path=%x_base:\scripts\=\%
echo.   -- Driver Path: %x_path%
echo.   -- Spark Home : %SPARK_HOME%

set JAVA_HOME=%JAVA17_HOME%

set x_sub=%SPARK_HOME%\bin\spark-submit --deploy-mode client
set x_cmd=%x_sub% %x_path%%x_driver%
echo. %x_cmd%
call %x_cmd%
set/a X_ERR=%ERRORLEVEL%
if X_ERR NEQ 0 goto submit_failed
goto fini

:
:submit_failed
echo.
echo. Submit failed, RC=%X_ERR%

:
:finis
endlocal
echo.
echo. Smoke Test Done
echo.