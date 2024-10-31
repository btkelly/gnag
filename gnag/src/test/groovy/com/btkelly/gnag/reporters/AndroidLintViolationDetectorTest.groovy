/*
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.btkelly.gnag.reporters

class AndroidLintViolationDetectorTest extends GroovyTestCase {

    void test_missingUrlsNode() {
        def actualUrls = AndroidLintViolationDetector.computeSecondaryUrls(
                null,
                "A newer version of com.android.support:appcompat-v7 than 23.3.0 is available: 24.0.0")

        assertEquals(new ArrayList<String>(), actualUrls)
    }

    void test_emptyUrlsNode() {
        def actualUrls = AndroidLintViolationDetector.computeSecondaryUrls(
                "",
                "A newer version of com.android.support:appcompat-v7 than 23.3.0 is available: 24.0.0")

        assertEquals(new ArrayList<String>(), actualUrls)
    }

    void test_urlsNodeWithSingleEntry_notIncludedInMessage() {
        def actualUrls = AndroidLintViolationDetector.computeSecondaryUrls(
                "http://developer.android.com/reference/android/os/Build.VERSION_CODES.html",
                "A newer version of com.android.support:appcompat-v7 than 23.3.0 is available: 24.0.0")

        def expectedUrls = new ArrayList<String>() {
            {
                add("http://developer.android.com/reference/android/os/Build.VERSION_CODES.html")
            }
        }

        assertEquals(expectedUrls, actualUrls)
    }

    void test_urlsNodeWithSingleEntry_includedInMessage() {
        def actualUrls = AndroidLintViolationDetector.computeSecondaryUrls(
                "https://developer.android.com/preview/backup/index.html",
                "On SDK version 23 and up, your app data will be automatically backed up and restored on app install. Consider adding the attribute `android:fullBackupContent` to specify an `@xml` resource which configures which files to backup. More info: https://developer.android.com/preview/backup/index.html")

        assertEquals(new ArrayList<String>(), actualUrls)
    }

    void test_urlsNodeWithMultipleEntries_noneIncludedInMessage() {
        def actualUrls = AndroidLintViolationDetector.computeSecondaryUrls(
                "https://developer.android.com/preview/backup/index.html,http://developer.android.com/reference/android/R.attr.html#allowBackup",
                "On SDK version 23 and up, your app data will be automatically backed up and restored on app install. Consider adding the attribute `android:fullBackupContent` to specify an `@xml` resource which configures which files to backup.")

        def expectedUrls = new ArrayList<String>() {
            {
                add("https://developer.android.com/preview/backup/index.html")
                add("http://developer.android.com/reference/android/R.attr.html#allowBackup")
            }
        }

        assertEquals(expectedUrls, actualUrls)
    }

    void test_urlsNodeWithMultipleEntries_someIncludedInMessage() {
        def actualUrls = AndroidLintViolationDetector.computeSecondaryUrls(
                "https://developer.android.com/preview/backup/index.html,http://developer.android.com/reference/android/R.attr.html#allowBackup",
                "On SDK version 23 and up, your app data will be automatically backed up and restored on app install. Consider adding the attribute `android:fullBackupContent` to specify an `@xml` resource which configures which files to backup. More info: https://developer.android.com/preview/backup/index.html")

        def expectedUrls = new ArrayList<String>() {
            {
                add("http://developer.android.com/reference/android/R.attr.html#allowBackup")
            }
        }

        assertEquals(expectedUrls, actualUrls)
    }

    void test_urlsNodeWithMultipleEntries_allIncludedInMessage() {
        def actualUrls = AndroidLintViolationDetector.computeSecondaryUrls(
                "https://developer.android.com/preview/backup/index.html,http://developer.android.com/reference/android/R.attr.html#allowBackup",
                "On SDK version 23 and up, your app data will be automatically backed up and restored on app install. Consider adding the attribute `android:fullBackupContent` to specify an `@xml` resource which configures which files to backup. More info: https://developer.android.com/preview/backup/index.html and http://developer.android.com/reference/android/R.attr.html#allowBackup")

        assertEquals(new ArrayList<String>(), actualUrls)
    }
}
