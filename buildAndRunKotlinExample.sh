#!/usr/bin/env bash

./gradlew clean publishGnagPublicationPublicationToMavenLocal

if [ $? -eq 0 ]; then
	cd example-kotlin/

	./gradlew clean gnagCheck

	cd ..
fi
