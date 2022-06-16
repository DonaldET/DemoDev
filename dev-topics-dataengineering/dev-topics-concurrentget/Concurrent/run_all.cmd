: run_all.cmd
:
: Run the three test classes, from the target directory, with timing
:
: Invocation: run_all.cmd 1> run_all.lst 2>&1
:
@echo off
echo.
echo. Run the 3 test classes for different levels of concurrency
echo.
set xx_cp=..\target\classes\
set JAVA_OPTIONS=-Xmx512M -Xms512M

echo.
echo. === SequentialRunner from %xx_cp% using %JAVA_OPTIONS%
java %JAVA_OPTIONS% -cp .;%xx_cp% don.demo.concurrent.SequentialRunner
if ERRORLEVEL 1 goto run1_failed

echo.
echo. === ConcurrentRunner from %xx_cp% using %JAVA_OPTIONS%
java %JAVA_OPTIONS% -cp .;%xx_cp% don.demo.concurrent.ConcurrentRunner
if ERRORLEVEL 1 goto run2_failed

echo.
echo. === HighlyConcurrentRunner from %xx_cp% using %JAVA_OPTIONS%
java %JAVA_OPTIONS% -cp .;%xx_cp% don.demo.concurrent.HighlyConcurrentRunner
if ERRORLEVEL 1 goto run3_failed

echo.
echo. Successfully Completed.
goto fini

:
:run1_failed
echo.
echo. SequentialRunner failed!
goto fini

:
:run2_failed
echo.
echo. ConcurrentRunner failed!
goto fini

:
:run3_failed
echo.
echo. HighlyConcurrentRunner failed!
goto fini

:
:fini
