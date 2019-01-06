#!/usr/bin/env bash

echo "Deleting any existing report..."

{
    pushd example-java-kotlin/
    ./gradlew clean
    popd
} &> /dev/null

echo "Generating a new report..."

./buildAndRunJavaKotlinExample.sh &> /dev/null

echo "Comparing new report to canonical report..."

diff test-resources/java-kotlin-gnag.html example-java-kotlin/build/outputs/gnag/gnag.html

if [ $? -eq 0 ]; then
    echo "New report matched canonical report; test passed!"
    exit 0
else
    echo "New report did not match canonical report; test failed!"
    exit 1
fi
