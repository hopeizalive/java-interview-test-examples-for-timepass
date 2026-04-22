@echo off
REM Java design patterns interview lessons CLI — run from repo root.
setlocal
pushd "%~dp0" || exit /b 1

if "%~1"=="" (
  call mvnw.cmd -pl java-design-patterns-interview-study compile exec:java
) else (
  call mvnw.cmd -pl java-design-patterns-interview-study compile exec:java "-Dexec.args=%*"
)

set EXITCODE=%ERRORLEVEL%
popd
exit /b %EXITCODE%
