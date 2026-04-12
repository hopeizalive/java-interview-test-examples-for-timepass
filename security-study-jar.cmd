@echo off
REM Run the packaged fat JAR (fast, no Maven). Build once: mvnw -pl spring-security-questions package
setlocal
pushd "%~dp0" || exit /b 1

set "JAR=spring-security-questions\target\spring-security-questions-1.0-SNAPSHOT.jar"
if not exist "%JAR%" (
  echo JAR not found: %JAR%
  echo Run: mvnw -pl spring-security-questions package
  popd
  exit /b 1
)

java -jar "%JAR%" %*
set EXITCODE=%ERRORLEVEL%
popd
exit /b %EXITCODE%
