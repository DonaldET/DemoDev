:
: loadPyBuilder.cmd
:
: Load PyBuilder build for project using virtual environment
:
@echo off
setlocal
set scp=venv\Scripts
set vactive=%scp%\activate.bat
set ve=NO
echo.
echo. Load PyBuilder using the virtual environment venv, but do not install dependencies
if not exist %vactive% goto NO_VE_ERR

echo.   -- Virtual environment setup
call %vactive%
if ERRORLEVEL == 1 goto NO_VE_ERR
set ve=YES

echo.
echo.   -- Install PyBuilder
python -m pip --version
python -m pip install --pre pybuilder --verbose
if ERRORLEVEL == 1 goto ERR_INST_PIP

echo.
echo. Repair PIP
python -m pip --version
python -m pip install --upgrade pip
if ERRORLEVEL == 1 goto ERR_INST_PYB
goto FINIS

:NO_VE_ERR
echo.
echo. No Virtual Environment Activate (%vactive%)!
set ve=NO
goto FINIS

:ERR_INST_PYB
echo.
echo. Windows install problem for PIP!
pip --version
echo.
goto FINIS

:FINIS
echo.
echo. Load effort complete - remove virtual environment
if "%ve%" == "yes" call %scp%\deactivate.bat

endlocal
echo.
echo. Done
echo.