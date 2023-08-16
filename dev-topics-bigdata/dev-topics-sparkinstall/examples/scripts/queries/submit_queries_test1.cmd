:: submit_queries_test1.cmd
: Examples: https://sparkbyexamples.com/spark/spark-submit-command/
::

@echo off
setlocal
set x_script=$0
set x_driver=TestPart1.py

echo.
echo. Summit Spark Queries Test 1 script using driver %x_driver% on %SPARK_HOME% executing %x_script%.cmd
set x_base=%~dp0
echo.   -- Base Path  : %x_base%
set x_path=%x_base:\scripts\=\%
echo.   -- Driver Path: %x_path%
echo.   -- Spark Home : %SPARK_HOME%

set JAVA_HOME=%JAVA17_HOME%
%JAVA_HOME%\bin\java -version

rmdir /s/q tmp

set x_cmd=%SPARK_HOME%\bin\spark-submit
set x_cmd=%x_cmd% %x_path%%x_driver%
set x_cmd=%x_cmd% --py-files part1.py
set x_cmd=%x_cmd% --files ..\..\queries\data\customers.csv, ..\..\queries\data\orders.csv, ..\..\queries\data\purchases.json
echo. %x_cmd%
call %x_cmd%
set/a x_rc=%ERRORLEVEL%
if %x_rc% EQU 0 goto finis
echo.
echo. Submit failed, RC=%x_rc%

:
:finis
endlocal
echo.
echo. Queries Tests 1 Script Done
echo.