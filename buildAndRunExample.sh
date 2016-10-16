#!/usr/bin/env bash

./gradlew clean --stacktrace --debug publishGnagPublicationPublicationToMavenLocal

if [ $? -eq 0 ]; then
	cd example/

	./gradlew clean gnagCheckDebug

	cd ..
fi
