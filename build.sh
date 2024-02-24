#!/bin/bash

function check_maven {
  if ! [ -x "$(command -v mvn)" ]; then
    echo "Error: Maven is not installed." >&2
    exit 1
  fi
}

check_maven

echo "Building the application..."

mvn clean install
