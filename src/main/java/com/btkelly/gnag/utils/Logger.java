/**
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
package com.btkelly.gnag.utils;

import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logging;

/**
 * Created by bobbake4 on 12/2/15.
 */
public class Logger {

    private static final String TAG = ":gnag: ";

    public static boolean isDebugLoggingEnabled() {
        return Logging.getLogger(Logger.class).isDebugEnabled();
    }

    public static boolean isInfoLoggingEnabled() {
        return Logging.getLogger(Logger.class).isInfoEnabled();
    }

    public static void logDebug(String message) {
        Logging.getLogger(Logger.class).log(LogLevel.DEBUG, TAG + message);
    }

    public static void logInfo(String message) {
        Logging.getLogger(Logger.class).log(LogLevel.INFO, TAG + message);
    }

    public static void logError(String message) {
        Logging.getLogger(Logger.class).log(LogLevel.ERROR, TAG + message);
    }
}
