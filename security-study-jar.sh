#!/bin/bash
# Run the packaged fat JAR (fast, no Maven). Build once: ./mvnw -pl spring-security-questions package
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$DIR" || exit 1

export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

JAR="spring-security-questions/target/spring-security-questions-1.0-SNAPSHOT.jar"
if [ ! -f "$JAR" ]; then
  echo "JAR not found: $JAR"
  echo "Run: ./mvnw -pl spring-security-questions package"
  exit 1
fi

java -jar "$JAR" "$@"