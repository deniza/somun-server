#!/bin/bash

# This script is used to run the application or setup the database
# Usage:
# > somun setup   ## to setup the database
# > somun run     ## to start the server application

function check_java {
  if ! [ -x "$(command -v java)" ]; then
    echo "Error: Java is not installed." >&2
    exit 1
  fi
}

function print_usage {
  echo "Usage: $0 [run|setup]"
}

function run {
  check_java
  java -jar target/Somun-1.0.jar
}

function setup {
  check_java
  java -jar target/Somun-1.0.jar --setup
}

if [ -z "$1" ]; then
  print_usage
elif [  "$1" == "run" ]; then
  run
elif [  "$1" == "setup" ]; then
  setup
else
  print_usage
fi
