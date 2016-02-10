#!/usr/bin/env bash

./gradlew clean assembleWithDependencies
cd example
../gradlew clean checkLocal --stacktrace
cd ..