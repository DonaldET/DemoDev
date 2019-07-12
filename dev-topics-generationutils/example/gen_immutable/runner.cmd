::
: runner.cmd
:
: Orchestrator example for Demo Generator
::
@echo off
echo.
echo. Orchestrator example for Demo Generator
echo.
type provenance.txt
echo.
call hive_runner.cmd
echo.
call extract_runner.cmd
echo.
echo. Done!
echo.
 