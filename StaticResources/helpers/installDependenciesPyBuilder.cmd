:
: installDependenciesPyBuilder.cmd
:
: Insall internal dependencies for PyBuilder in the virtual environment
:
@echo off
setlocal
set scp=venv\Scripts
set vactive=%scp%\activate.bat
set ve=NO
echo.
echo. Install dependencies for PyBuilder using the virtual environment venv
if not exist %vactive% goto NO_VE_ERR

echo.   -- Virtual environment setup
call %vactive%
if ERRORLEVEL == 1 goto NO_VE_ERR
set ve=YES

echo.   -- builder.py
set bldr=build.py
if not exist %bldr% goto NO_BUILDER_ERR

echo.   -- PyBuilder
set pyb=%scp%\pyb_.exe
if not exist %pyb% goto NO_PYB_ERR

echo.
echo.   -- Install PyBuilder Dependencies
python -m pip --version
%pyb% install_dependencies
if ERRORLEVEL == 1 goto ERR_PYB_FAILED

echo.
echo. Repair PIP
python -m pip --version
python -m pip install --upgrade pip
if ERRORLEVEL == 1 goto ERR_PYB_INST
goto FINIS

:NO_VE_ERR
echo.
echo. No Virtual Environment Activate (%vactive%)!
set ve=NO
goto FINIS

:NO_BUILDER_ERR
echo.
echo. No build file (%bldr%)!
goto FINIS

:NO_PYB_ERR
echo.
echo. No PyBuilder executable (%pyb%)!
goto FINIS

:ERR_PYB_FAILED
echo.
echo. Dependency install problem for PyBuilder!
pip --version
echo.
goto FINIS

:FINIS
echo.
echo. Install dependencies effort complete - remove virtual environment
if "%ve%" == "yes" call %scp%\deactivate.bat

endlocal
echo.
echo. Done
echo.