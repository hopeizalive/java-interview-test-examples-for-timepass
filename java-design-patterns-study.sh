#!/bin/bash
# Java design patterns interview lessons CLI — run from repo root.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$DIR" || exit 1

if [ $# -eq 0 ]; then
  ./mvnw -pl java-design-patterns-interview-study compile exec:java
else
  ./mvnw -pl java-design-patterns-interview-study compile exec:java -Dexec.args="$*"
fi
