:: run_parallel_runner_foreach_concurrent.cmd
:
: Run the parallel runner main program for foreach with concurrent hash map
::
@echo off
echo. Running run_parallel_runner_foreach_concurrent.cmd
set main_class=don.demo.algo.cpuconcurrent.ParallelRunner
echo.   -- Main Class: %main_class%
set main_selector=2
echo.   -- Selector  : %main_selector%
timer /nologo
java -cp .;target\demodev-topics-cpuconcurrent-1.0.0-SNAPSHOT.jar %main_class% %main_selector%
if ERRORLEVEL == 1 goto run_label_error
echo.
echo.  -- Succeeded
goto run_label_fini

:
:run_label_error
echo. Run failed!
goto run_label:done

:
:run_label_fini

:
:run_label_done
timer /nologo /et /s
echo. Run completed
