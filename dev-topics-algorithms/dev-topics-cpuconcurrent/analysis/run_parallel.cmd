::
: run_parallel.cmd
:
: Run the parallel stream timer runner for different types of collection mechanisms
: for both parallel and sequential operation.
@echo off
echo.
echo. Run the parallel stream timer runner for different types of collection mechanisms
echo. for both parallel and sequential operation.
echo.

setlocal
set xx_cp=..\target
set xx_jar=demodev-topics-cpuconcurrent-1.0.0-SNAPSHOT.jar
set xx_runner=don.demo.algo.cpuconcurrent.ParallelRunner
set xx_cmd=java -Djava.util.concurrent.ForkJoinPool.common.parallelism=7 -cp .;%xx_cp%\%xx_jar% %xx_runner%
echo. $ %xx_cmd%
echo.
echo.
echo. List Collector
%xx_cmd% 0

echo.
echo.
echo. forEach with Synchronized HashMap
%xx_cmd% 1

echo.
echo.
echo. forEach with Concurrent HashMap
%xx_cmd% 2

:
:finis
echo.
echo. . . . done
endlocal