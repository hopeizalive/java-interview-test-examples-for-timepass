#!/usr/bin/env bash
set -euo pipefail

# run-all-study-modules.sh
# Run all four study module launcher scripts in one sequence.
# This makes it easy to test all modules in one go.

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"
cd "$DIR" || exit 1

SCRIPTS=(
  "./concurrency-study.sh"
  "./microservices-study.sh"
  "./security-study.sh"
  "./spring-data-study.sh"
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
  echo "Starting: $script"
  echo "================================================================="
  "$script"
  echo "================================================================="
  echo "Completed: $script"
  echo "================================================================="
  echo

done

echo "All study modules finished successfully."
