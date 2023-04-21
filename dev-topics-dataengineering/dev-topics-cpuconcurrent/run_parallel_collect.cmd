:: run_parallel_collect.cmd
:
: Run the parallel collection main program
::
@echo off
echo. Running run_parallel_collect.cmd
set main_class=don.demo.algo.cpuconcurrent.ParallelCollect
echo.   -- Main Class: %main_class%
timer /nologo
java -cp .;target\demodev-topics-cpuconcurrent-1.0.0-SNAPSHOT.jar %main_class%
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
