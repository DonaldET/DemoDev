::
: countFiles.cmd - 2014.9.3
:
: Count Project Input Files
:
: Grep [-1abcdefghijlnopqrstuvwxz?] searchstring [filename[s]] [@listfile]
: Options can be followed by a '+' (on, default) or '-' (off).
: Following are the options explained followed by their default:
:  -1- Only first match   -h- No file names        -s- Silent, errors only
:  -2- No duplicate files -i- Ignore case          -t- Truncate output to < 80
:  -a- Recognize UNICODE  -j- Filter output        -u  Update default options
:  -b- Binary search      -l- File names only      -v- Non-matching lines only
:  -c- Match count only   -n- Line numbers         -w- Word search [0-9a-zA-Z_]
:  -d- Recurse subdirs    -o- UNIX output format   -x-0+0 Context lines
:  -e+ Include errors     -p  Specify start        -z- Verbose
:  -f- Fast stdin         -q  Specify end          -?  Additional help
:  -g- Total searched     -r+ Regular expressions
: Special characters for regular expressions in the search string:
:   ^ start of line      $ end of line        . any character
:   ~ any non-ASCII character     \ quote next character
:   ? match previous 0 or 1       \xhh hh (hex) specifies an ASCII character
:   * match previous 0 or more    + match previous 1 or more
:   ( ) treat as one unit         | separates alternatives, bounded by ()
: [ac0-3] match a,c or 0 through 3    [^a-z]  match anything but a through z
::

@echo off
setlocal
set XX_CurDir=%CD%
set XX_grep=dkgrep
echo.
echo. **** Count of Repository Development Files and Resources
echo.

:
:xx_setup_base
set XX_pr1=
call :XX_GetPath %0
if not defined XX_pr1 goto XX_badPath
pushd %XX_pr1%\..
if ERRORLEVEL 1 goto XX_badPath

set XX_cmd=%XX_grep% -d -s -1 "^" pom.xml
%XX_cmd% 1>nul 2>nul
echo. MAVEN POM files: %ERRORLEVEL%

set XX_cmd=%XX_grep% -d -s -1 "." *.class
%XX_cmd% 1>nul 2>nul
echo. Class Files    : %ERRORLEVEL%

set XX_cmd=%XX_grep% -d -s -1 "." *.jar
%XX_cmd% 1>nul 2>nul
echo. Archive Files  : %ERRORLEVEL%

set XX_cmd=%XX_grep% -d -s -1 "." *.war
%XX_cmd% 1>nul 2>nul
echo. WAR Files      : %ERRORLEVEL%

set XX_cmd=%XX_grep% -d -s -1 "^" *.java
%XX_cmd%
echo. Java files     : %ERRORLEVEL%

set XX_cmd=%XX_grep% -d -s "^" *.java
%XX_cmd%
echo. Java lines     : %ERRORLEVEL%

set /A XX_Count = 0

set XX_cmd=%XX_grep% -d -s "^" Test*.java
%XX_cmd%
set /A XX_Count=XX_Count + %ERRORLEVEL%

set XX_cmd=%XX_grep% -d -s "^" *Test.java
%XX_cmd%
set /A XX_Count=XX_Count + %ERRORLEVEL%

set XX_cmd=%XX_grep% -d -s "^" *Tests.java
%XX_cmd%
set /A XX_Count=XX_Count + %ERRORLEVEL%

echo. Java Test lines: %XX_Count%

set XX_cmd=%XX_grep% -d -s -1 "^" *.xml
%XX_cmd%
echo. XML files      : %ERRORLEVEL%

goto finis

:
:finis
echo.
if defined XX_CurDir popd %XX_CurDir%
set XX_CurDir=
set XX_grep=
set XX_cmd=
set XX_Count=
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
: end of countFiles.cmd
::