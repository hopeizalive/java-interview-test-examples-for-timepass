@echo off
REM Spring Boot annotation comparison lessons CLI — run from anywhere; switches to this repo root first.
setlocal
pushd "%~dp0" || exit /b 1

if "%~1"=="" (
  call mvnw.cmd -pl spring-annotation-interview-study compile exec:java
) else (
  call mvnw.cmd -pl spring-annotation-interview-study compile exec:java "-Dexec.args=%*"
)

set EXITCODE=%ERRORLEVEL%
popd
exit /b %EXITCODE%
