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

import org.jetbrains.annotations.Nullable;

public final class StringUtils {

    /**
     * Removes leading/trailing whitespace and newlines.
     */
    public static String sanitize(@Nullable final String string) {
        if (string == null) {
            return null;
        }

        return string
                .trim()
                .replaceAll("^\\r|^\\n|\\r$\\n$", "");
    }
    
    private StringUtils() {
        // This constructor intentionally left blank.
    }
    
}
