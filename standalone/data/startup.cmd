@echo off
setlocal ENABLEDELAYEDEXPANSION
set "CP=classes"
for /r %%i in ("jar\*.jar") do call set CP=!CP!;%%i

set "JVM_ARGS="
set "JVM_ARGS=%JVM_ARGS% -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
set "JVM_ARGS=%JVM_ARGS% -ea"
set "JVM_ARGS=%JVM_ARGS% -classpath %CP%"

set "MAIN_CLASS=net.kkolyan.trainingdroid.standalone.StandaloneLauncher"

java %JVM_ARGS% %MAIN_CLASS%