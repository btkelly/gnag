#!/usr/bin/env bash

echo "Deleting any existing report..."

{
    pushd example-android/
    ./gradlew app:clean
    popd
} &> /dev/null

echo "Generating a new report..."

./buildAndRunAndroidExample.sh &> /dev/null

echo "Comparing new report to canonical report..."

ls
ls example-android
ls example-android/app
ls example-android/app/build
ls example-android/app/build/outputs
ls example-android/app/build/outputs/gnag
diff test-resources/android-gnag.html example-android/app/build/outputs/gnag/gnag.html

if [ $? -eq 0 ]; then
    echo "New report matched canonical report; test passed!"
    exit 0
else
    echo "New report did not match canonical report; test failed!"
    exit 1
fi
