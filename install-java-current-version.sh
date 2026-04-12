#!/usr/bin/env bash
set -euo pipefail

# install-java-current-version.sh
# Installs the same Java version currently configured on this machine using SDKMAN,
# without sudo. If the exact version is already installed, it does nothing.

SDKMAN_INIT="$HOME/.sdkman/bin/sdkman-init.sh"
if [[ ! -s "$SDKMAN_INIT" ]]; then
  echo "ERROR: SDKMAN is not installed. Install SDKMAN first: https://sdkman.io/install"
  exit 1
fi

# shellcheck source=/dev/null
source "$SDKMAN_INIT"

if ! command -v sdk >/dev/null 2>&1; then
  echo "ERROR: sdk command not found after sourcing SDKMAN."
  exit 1
fi

# Determine the currently selected Java version identifier from SDKMAN.
CURRENT_SDKMAN_JAVA=""
if sdk current java >/dev/null 2>&1; then
  CURRENT_SDKMAN_JAVA=$(sdk current java | awk -F'Using java version ' '{print $2}' | tr -d '\r')
fi

if [[ -z "$CURRENT_SDKMAN_JAVA" ]]; then
  echo "ERROR: Unable to determine current Java version from SDKMAN."
  exit 1
fi

DESIRED_JAVA_ID="$CURRENT_SDKMAN_JAVA"

echo "Desired Java version identifier: $DESIRED_JAVA_ID"

# Check whether this version is already installed.
INSTALLED_LINE=$(sdk list java | grep -E "[[:space:]]$DESIRED_JAVA_ID([[:space:]]|")" || true)

if [[ -n "$INSTALLED_LINE" && "$INSTALLED_LINE" == *"local only"* ]] || [[ -n "$INSTALLED_LINE" && "$INSTALLED_LINE" == *"installed"* ]] || [[ "$CURRENT_SDKMAN_JAVA" == "$DESIRED_JAVA_ID" ]]; then
  echo "Java $DESIRED_JAVA_ID is already installed."
  if [[ "$CURRENT_SDKMAN_JAVA" != "$DESIRED_JAVA_ID" ]]; then
    echo "Setting Java $DESIRED_JAVA_ID as the default SDKMAN version."
    sdk default java "$DESIRED_JAVA_ID"
  fi
  exit 0
fi

# Install the desired version without sudo.
echo "Installing Java $DESIRED_JAVA_ID using SDKMAN..."
sdk install java "$DESIRED_JAVA_ID"

echo "Java $DESIRED_JAVA_ID installation complete."
exit 0
