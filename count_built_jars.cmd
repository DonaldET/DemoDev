::
: count_built_jars.cmd
:
: Drop through all directories finding jar files, but ingore the 'original' files created by shading
::
@echo off
echo.
setlocal
echo. Counting Jar files from Project Root
set myDir=xxxx
for %%F in (%0) do set myDir=%%~dpF
echo.                     working from: %myDir%
pushd %myDir%

dir/s/b *SNAPSHOT.jar | find "\target\" | find /v "original-" | find /v "\example\" | find /c /v ""


:
:fini
popd
echo.
echo. Built Jar count Done
endlocal
