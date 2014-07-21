::
: tomcat-archetype.cmd
:
:  Sample run:
: 
: Creating project dev-topics-jersey-safecollection
: $ mvn archetype:generate \
:   -DarchetypeGroupId=org.glassfish.jersey.archetypes \
:   -DarchetypeArtifactId=jersey-quickstart-webapp \
:   -DgroupId=dev-topics \
:   -DartifactId=jersey-safecollection-service \
:   -Dpackage=demo.jersey.safecollection.service \
:   -DarchetypeVersion=2.10.1 \
:   -DinteractiveMode=false
::
@echo off
setlocal

set projectName=dev-topics-jersey-safecollection
set archGroupId=org.glassfish.jersey.archetypes
set archArtifactId=jersey-quickstart-webapp
set archVersion=2.10.1

set P1=%1

set demogroupId=dev-topics
set demoartifact=jersey-safecollection-service
set demopackage=demo.jersey.safecollection.service

echo. Creating project %projectName%
if  defined P1 echo.  Modifiers:   %P1%

set cmdline=mvn archetype:generate

:
:no_extra_option
set cmdline=%cmdline% -DarchetypeGroupId=%archGroupId%
set cmdline=%cmdline% -DarchetypeArtifactId=%archArtifactId%
set cmdline=%cmdline% -DgroupId=%demogroupId%
set cmdline=%cmdline% -DartifactId=%demoartifact%
set cmdline=%cmdline% -Dpackage=%demopackage%
set cmdline=%cmdline% -DarchetypeVersion=%archVersion%
set cmdline=%cmdline% -DinteractiveMode=false
if  defined P1 set cmdline=%cmdline% %P1%
echo. $ %cmdline%
%cmdline%
if ERRLEVEL 1 goto gen_error
echo.
echo. Successfully generated %projectName%
goto finis

:
:gen_error
echo.
echo. *** Generation of %projectName% failed!
goto finis

:finis
set demoprojectName=
set archGroupId=
set archArtifactId=
set archVersion=2.10.1

set demogroupId=
set demoartifact=
set demopackage=

set P1=
set cmdline=
endlocal
::
: tomcat-archetype.cmd
::