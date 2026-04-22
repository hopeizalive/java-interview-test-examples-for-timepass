#!/bin/bash
# Java data structures / collections interview lessons CLI — run from anywhere; switches to this repo root first.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$DIR" || exit 1

export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

if [ $# -eq 0 ]; then
  ./mvnw -pl java-ds-interview-study compile exec:java
else
  ./mvnw -pl java-ds-interview-study compile exec:java -Dexec.args="$*"
fi
