::
: get_pom_tokens.cmd
:
: Locates maven plugin entries in nested pom.xml files defined by the script findPlugins.cmd. It then
: 
: Notes:
:   o run mvn clean prior to scan.
:   o Script findPlugins.cmd knows where project root is, and is relative to itself
:   o The script knows were findPlugins.cmd is relative to itself
:   o The 'helpers' package must be installed in the virtual environment (see install_helpers.cmd)
:   o Running python helpers must be done in the virtual environment
::
@echo off
echo.
setlocal
set finder=findPlugins.cmd
echo. Listing MAVEN plugins in POMs using: %finder%
set finderPath=..\helpers\src\unittest\resources
echo.                          located in: %finderPath%
set lst=maven-plugins.txt
echo.                      into text file: %lst%

set myDir=xxxx
for %%F in (%0) do set myDir=%%~dpF
echo.                        working from: %myDir%
:pushd %myDir%

echo.
echo. WARNING: Execute maven clean first!
echo.

call %finderPath%\%finder% > %lst%
if ERRORLEVEL == 1 goto extract_mods_failed
echo. Done creating %lst%
goto setup_list_mods

:
:extract_mods_failed
echo.
echo. ERROR: extract models from POM files failed!
echo.
goto fini

:
:setup_list_mods
set act=..\helpers\venv\Scripts
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
echo.
files_with_tokens "<artifactId>" "</artifactId>" "%lst%"
if ERRORLEVEL 1 goto fwt_failed
echo.
goto cleanup

:
:fwt_failed
echo.
echo. ERROR: Unable to list tokens in %lst%
echo.
goto cleanup

:
:cleanup
call %act%\deactivate.bat
goto fini

:
:fini
popd
endlocal
