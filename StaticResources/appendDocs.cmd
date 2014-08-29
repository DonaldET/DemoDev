::
: appendDocs.cmd - 2014.8.11
:
: Assemble README.md from docs sub-directory
::

@echo off
setlocal
set XX_CurDir=%CD%
echo.
echo. **** Assembling README.md

:
:xx_setup_base
set XX_pr1=
call :XX_GetPath %0
if not defined XX_pr1 goto XX_badPath
pushd %XX_pr1%
if ERRORLEVEL 1 goto XX_badPath
goto xx_setup_files

:xx_badPath
echo.
echo. -- Failed to determine path "%XX_pr1%"
set XX_pr1=
goto finis

:
:xx_setup_files
set XX_srcDir=..\docs\
set XX_masterFile=README.md
set XX_targetDir=..\
set XX_allDocs=%XX_targetDir%%XX_masterFile%

if exist %XX_allDocs% del /F/Q %XX_allDocs%
copy /a /y nul %XX_allDocs% > nul

echo.   Append all files from %XX_srcDir%*.md into %XX_allDocs%
for %%f in (%XX_srcDir%*.md) do (
echo.     %%f
type %%f >> %XX_allDocs%
)
goto finis

:
:finis
echo. **** Assembling README.md completed
if defined XX_CurDir popd %XX_CurDir%

set XX_CurDir=
set XX_pr1=
set XX_srcDir=
set XX_masterFile=
set XX_targetDir=
set XX_allDocs=
endlocal
goto Done

:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

::
: Determine directory path
::
:XX_GetPath
set XX_pr1=
for %%I in (%1) do set XX_pr1=%%~dpI
goto :EOF

:
:Done
pause

::
: end of appendDocs.cmd
::