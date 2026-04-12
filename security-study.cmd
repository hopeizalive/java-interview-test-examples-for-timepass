@echo off
REM Spring Security lessons CLI — run from anywhere; switches to this repo root first.
setlocal
pushd "%~dp0" || exit /b 1

if "%~1"=="" (
  call mvnw.cmd -pl spring-security-questions compile exec:java
) else (
  call mvnw.cmd -pl spring-security-questions compile exec:java "-Dexec.args=%*"
)

set EXITCODE=%ERRORLEVEL%
popd
exit /b %EXITCODE%
