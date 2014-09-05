::
: cleanFiles.cmd - 2014.8.29
:
: Cleanup Build files
::

@echo off
setlocal
set XX_CurDir=%CD%
echo.
echo. **** Cleaning Build Files

:
:xx_setup_base
set XX_pr1=
call :XX_GetPath %0
if not defined XX_pr1 goto XX_badPath
pushd %XX_pr1%\..
if ERRORLEVEL 1 goto XX_badPath

echo. Cleaning MAVEN artifacts
call mvn clean
if ERRORLEVEL 1 goto XX_mavenbad
echo. MAVEN clean done
goto XX_mavendone
:XX_mavenbad
echo. MAVEN failed!
goto finis

:XX_mavendone
echo. Cleaning MAVEN eclipse artifacts
call mvn clean
if ERRORLEVEL 1 goto XX_mavenECbad
echo. MAVEN clean done
goto XX_mavenECdone
:XX_mavenECbad
echo. MAVEN Eclipse failed!
goto finis

:XX_mavenECdone
set XX_File2Remove=.settings
echo.   Remove all instances of %XX_File2Remove%
for /R %%f in (%XX_File2Remove%\) do (
if EXIST %%f rmdir /S /Q %%f
)

set XX_File2Remove=dependency-reduced-pom.xml
echo.   Remove all instances of %XX_File2Remove%
for /R %%f in (%XX_File2Remove%) do (
if EXIST %%f del /S /Q /F %%f
)
goto finis

:xx_badPath
echo.
echo. -- Failed to determine path "%XX_pr1%"
set XX_pr1=
goto finis

:
:finis
echo. **** Cleaning Build Files completed
if defined XX_CurDir popd %XX_CurDir%
set XX_CurDir=
set XX_pr1=
set XX_File2Remove=
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
: end of cleanFiles.cmd
::