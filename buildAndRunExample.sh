#!/usr/bin/env bash

./gradlew clean publishGnagPublicationPublicationToMavenLocal

if [ $? -eq 0 ]; then
	cd example/

	./gradlew clean gnagCheckDebug

	cd ..
fi
