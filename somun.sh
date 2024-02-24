#!/bin/bash

# This script is used to run the application or setup the database
# Usage:
# > somun setup   ## to setup the database
# > somun run     ## to start the server application

function run {
  java -jar target/Somun-1.0.jar
}

function setup {
  java -jar target/Somun-1.0.jar --setup
}

if [ -z "$1" ]; then
  echo "Usage: $0 [run|setup]"
elif [  "$1" == "run" ]; then
  run
elif [  "$1" == "setup" ]; then
  setup
fi
