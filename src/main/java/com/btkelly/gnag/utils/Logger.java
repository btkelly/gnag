/**
 * Copyright 2015 Bryan Kelly
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
package com.btkelly.gnag.utils;

/**
 * Created by bobbake4 on 12/2/15.
 */
public class Logger {

    private static final String TAG = ":gnag: ";

    private static boolean debugLogEnabled;

    public static void setDebugLogEnabled(boolean debugLogEnabled) {
        Logger.debugLogEnabled = debugLogEnabled;
    }

    public static boolean isDebugLogEnabled() {
        return debugLogEnabled;
    }

    public static void logD(String message) {
        if (debugLogEnabled) {
            System.out.println(TAG + message);
        }
    }

    public static void logE(String message) {
        System.out.println(TAG + message);
    }
}
