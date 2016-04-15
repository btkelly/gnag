#!/usr/bin/env bash

./gradlew clean publishGnagPublicationPublicationToMavenLocal

cd example/

./gradlew clean gnagCheck

cd ..