@echo off
setlocal EnableExtensions
REM Run Maven from IntelliJ's bundled installation (paths with spaces are quoted).
REM If your IDE path differs, set IDEA_MVN before calling, or edit the default below.

if not defined IDEA_MVN (
  set "IDEA_MVN=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1\plugins\maven\lib\maven3\bin\mvn.cmd"
)

if not exist "%IDEA_MVN%" (
  echo ERROR: Maven not found at:
  echo   %IDEA_MVN%
  echo Set IDEA_MVN to your mvn.cmd path, e.g.:
  echo   set IDEA_MVN=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1\plugins\maven\lib\maven3\bin\mvn.cmd
  exit /b 1
)

pushd "%~dp0"
"%IDEA_MVN%" -f "%~dp0pom.xml" %*
set "ERR=%ERRORLEVEL%"
popd
exit /b %ERR%
