#!/bin/bash
# Run the packaged fat JAR (fast, no Maven). Build once: ./mvnw -pl spring-microservices-questions package
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$DIR" || exit 1

export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

JAR="spring-microservices-questions/target/spring-microservices-questions-1.0-SNAPSHOT.jar"
if [ ! -f "$JAR" ]; then
  echo "JAR not found: $JAR"
  echo "Run: ./mvnw -pl spring-microservices-questions package"
  exit 1
fi

java -jar "$JAR" "$@"