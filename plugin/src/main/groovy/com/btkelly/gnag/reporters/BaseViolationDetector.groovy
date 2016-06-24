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

import org.gradle.api.Project

import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull

abstract class BaseViolationDetector implements ViolationDetector {

    protected final Project project

    BaseViolationDetector(final Project project) {
        this.project = project
    }

    /**
     * @param absoluteFilePath the absolute path to a target file. Should be trimmed of all excess whitespace and
     *                         newlines. Can be null.
     * @return the path to the target file, relative to the root directory of the current project, iff the target file
     *         is contained within that directory; null otherwise
     */
    protected String computeFilePathRelativeToProjectRoot(final String absoluteFilePath) {
        final String sanitizedAbsoluteFilePath = sanitizeToNonNull(absoluteFilePath)

        final String expectedFilePathPrefix = project.getRootDir().getAbsolutePath() + "/";

        if (!sanitizedAbsoluteFilePath.startsWith(expectedFilePathPrefix)) {
            // The target file _is not_ contained within the root directory of the current project. Return null.
            return null;
        } else {
            /*
             * The target file _is_ contained within the root directory of the current project. Return the path to the
             * target file relative to the root directory.
             */
            return sanitizedAbsoluteFilePath.replace(expectedFilePathPrefix, "");
        }
    }
    
}
