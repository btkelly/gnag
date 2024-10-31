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
package com.btkelly.gnag.reporters.utils

import org.gradle.api.logging.Logger

final class LineNumberParser {

    /**
     * @param lineNumberString a String we would like to parse into an integer value.
     * @param reporterName the name of the reporter currently running. Used for logging only.
     * @param violationType the name of the violation currently being parsed. Used for logging only.
     * @return a positive integer, if lineNumberString can be parsed into such a value; null in any other case.
     */
    static Integer parseLineNumberString(
            final String lineNumberString,
            final String reporterName,
            final String violationType,
            final Logger logger) {

        if (lineNumberString.isEmpty()) {
            return null
        } else {
            try {
                final int result = lineNumberString.toInteger()

                if (result >= 0) {
                    return result
                } else {
                    logger.error(
                            "Invalid line number: + " + result +
                                    " for " + reporterName + " violation: " + violationType)

                    return null
                }
            } catch (final NumberFormatException ignored) {
                logger.error(
                        "Error parsing line number string: \"" + lineNumberString +
                                "\" for " + reporterName + " violation: " + violationType)

                return null
            }
        }
    }

    private LineNumberParser() {
        // This constructor intentionally left blank.
    }

}
