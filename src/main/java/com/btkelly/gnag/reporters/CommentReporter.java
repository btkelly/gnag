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
package com.btkelly.gnag.reporters;

import org.gradle.api.Project;

/**
 * Interface for a comment reporter
 */
public interface CommentReporter {

    /**
     * This method should inspect the current project and return a true or false if this reporter should cause the build to fail.
     * @param project - current project being built
     * @return - return true to fail the current build false otherwise
     */
    boolean shouldFailBuild(Project project);

    /**
     * This method should inspect the project and return true or false if this reporter is enabled
     * @param project
     * @return - return true if the reporter is enabled false otherwise
     */
    boolean reporterEnabled(Project project);

    /**
     * This method should generate the text it would like added to the Github comment that will be posted at the end of the build.
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    String textToAppendComment(Project project);

    /**
     * Return the reporter name
     * @return - reporter name
     */
    String reporterName();
}
