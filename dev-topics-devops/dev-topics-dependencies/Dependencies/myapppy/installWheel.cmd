::
: installWhell.cmd
:
: Install wheel file defining package.
::
@echo off
setlocal
echo.
echo. Install Wheel File
set workDir=XXXX
for %%f in (%0) do set workDir=%%~dpf
echo.   -- Script in %workDir%
cd %workDir%

:
: Define package and while file location relative to this file
:
set packageName=myapppy
echo.   -- Package name: %packageName%
set wheelFile=target\dist\myapppy-1.0.dev0\dist\myapppy-1.0.dev0-py3-none-any.whl
set whlPath=%workDir%%wheelFile%
echo.   -- Wheel file in %whlPath%
if exist %whlPath% goto ACTIVATE
echo.
echo. %whlPath% does not exist!
goto FINIS

:
:ACTIVATE
set scp=venv\Scripts
set vactive=%scp%\activate.bat
if not exist %vactive% goto NO_VE_ERR
set ve=NO
echo.   -- Virtual environment setup
call %vactive%
if ERRORLEVEL == 1 goto NO_VE_ERR
set ve=YES
echo.   -- Uninstall %packageName%
python -m pip uninstall %packageName%
if ERRORLEVEL 1 goto PIP_UNINST_FAILED
goto CHECK_PIP

:
:PIP_UNINST_FAILED
echo. Note: pip uninstall of %packageName% failed!

:
:CHECK_PIP
echo.   -- Check PIP
python -m pip --version

echo.   -- Install %packageName% from %wheelFile%
python -m pip install "%whlPath%"
if ERRORLEVEL 1 goto PIP_INST_FAILED
echo.   Install Succeeded!
goto FINIS

:
:PIP_INST_FAILED
echo.
echo. Install of package $packageName% failed!
goto FINIS

:NO_VE_ERR
echo.
echo. No Virtual Environment Activate (%vactive%)!
set ve=NO
goto FINIS

:FINIS
echo.
echo. Install Wheel complete
if "%ve%" == "YES" (
echo.   -- Remove Virtual Environment
call %scp%\deactivate.bat
)
popd
endlocal
echo.
echo. Done
echo.