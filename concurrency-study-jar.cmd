@echo off
REM Run the packaged fat JAR (fast, no Maven). Build once: mvnw -pl concurrency-interview-study package
setlocal
pushd "%~dp0" || exit /b 1

set "JAR=concurrency-interview-study\target\concurrency-interview-study-1.0-SNAPSHOT.jar"
if not exist "%JAR%" (
  echo JAR not found: %JAR%
  echo Run: mvnw -pl concurrency-interview-study package
  popd
  exit /b 1
)

java -jar "%JAR%" %*
set EXITCODE=%ERRORLEVEL%
popd
exit /b %EXITCODE%
