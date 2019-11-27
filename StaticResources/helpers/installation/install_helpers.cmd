::
: install_helpers.cmd
:
: Install 'helpers' from a WHEEL file, but uninstall first. Must operate under the virtual environment.
::
@echo off
echo.
setlocal
set helper_pkg=helpers
set whlFile=helpers-1.0.dev0-py3-none-any.whl
echo. Loading wheel file distributable: %whlFile%
set distDir=..\..\helpers\target\dist\helpers-1.0.dev0\dist
echo.                distribution path: %distDir%
set myDir=xxxx
for %%F in (%0) do set myDir=%%~dpF
echo.                     working from: %myDir%
set distFile=%distDir%\%whlFile%
if exist %distFile% goto got_dist_file
echo.
echo. ERROR: %distFile% does not exist!
echo.
goto fini

:
:got_dist_file
set act=..\venv\Scripts
if exist "%act%\activate.bat" goto got_venv
echo.
echo. ERROR: unable to activate using %act%\activate
goto fini

:
:got_venv
call %act%\activate.bat
if not "%VIRTUAL_ENV%" == "" goto got_active
echo.
echo. ERROR: failed to activate!
echo.
goto fini

:
:got_active
echo.  -- de-installing %whlFile%
python -m pip uninstall %helper_pkg%
if ERRORLEVEL 1 goto uninst_failed

echo. -- installing %whlFile%
python -m pip install %distFile% -v
if ERRORLEVEL 1 goto inst_failed
echo. Install succeeded!
goto cleanup

:
:uninst_failed
echo.
echo. WARNING: Uninstall of %whlFile% failed
echo.
call %act%\deactivate.bat
goto fini

:
:inst_failed
echo.
echo. Install failed!
echo.

:
:cleanup
call %act%\deactivate.bat
goto fini

:fini
echo.
echo. Done with (%distFile%)
endlocal
