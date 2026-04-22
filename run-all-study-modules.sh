#!/usr/bin/env bash
set -euo pipefail

# run-all-study-modules.sh
# Run all study module launcher scripts (.sh) in one sequence.
# This makes it easy to test all modules in one go.

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
cd "$DIR" || exit 1

if [[ $# -ne 1 || "$1" != "run-all" ]]; then
  cat <<EOF
Usage: $0 run-all

Run all study module scripts in sequence:
  concurrency-study.sh
  microservices-study.sh
  security-study.sh
  spring-data-study.sh
  java-ds-study.sh
  spring-annotation-study.sh
EOF
  exit 1
fi

SCRIPTS=(
  "./concurrency-study.sh"
  "./microservices-study.sh"
  "./security-study.sh"
  "./spring-data-study.sh"
  "./java-ds-study.sh"
  "./spring-annotation-study.sh"
)

echo "Running all study modules from $DIR"

for script in "${SCRIPTS[@]}"; do
  if [[ ! -f "$script" ]]; then
    echo "ERROR: Expected script not found: $script"
    exit 1
  fi
  if [[ ! -x "$script" ]]; then
    echo "Making $script executable"
    chmod +x "$script"
  fi

done

for script in "${SCRIPTS[@]}"; do
  echo
  echo "================================================================="
  echo "Starting: $script run-all"
  echo "================================================================="
  "$script" run-all
  echo "================================================================="
  echo "Completed: $script run-all"
  echo "================================================================="
  echo

done

echo "All study modules finished successfully."
