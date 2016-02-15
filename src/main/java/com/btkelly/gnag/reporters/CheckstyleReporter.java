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

import com.btkelly.gnag.models.checkstyle.Checkstyle;
import com.btkelly.gnag.models.checkstyle.Error;
import com.btkelly.gnag.models.checkstyle.File;
import com.btkelly.gnag.utils.HtmlStringBuilder;

import java.io.FilenameFilter;

/**
 * Comment reporter for Checkstyle. Looks for Checkstyle report in the default location "/build/outputs/checkstyle/checkstyle.xml"
 */
public class CheckstyleReporter extends BaseReporter<Checkstyle> {

    /**
     * Loops through all Checkstyle errors and pulls out file name, line number, error message, and rule
     * @param report - parsed report object
     * @param projectDir - base project directory
     * @param htmlStringBuilder - StringBuilder to append the comment to
     */
    @Override
    public void appendViolationText(Checkstyle report, String projectDir, HtmlStringBuilder htmlStringBuilder) {

        for (File checkstyleFile : report.getFile()) {

            String fileName = checkstyleFile.getName();
            fileName = fileName.replace(projectDir, "");
            fileName = fileName.replace("/src/main/java/", "");
            fileName = fileName.replace("/", ".");

            for (Error checkstyleError : checkstyleFile.getError()) {
                htmlStringBuilder
                        .appendBold("Violation: ")
                        .append(checkstyleError.getSource())
                        .insertLineBreak()
                        .appendBold("Class: ")
                        .append(fileName)
                        .insertLineBreak()
                        .appendBold("Line: ")
                        .append(checkstyleError.getLine())
                        .insertLineBreak()
                        .appendBold("Notes: ")
                        .append(checkstyleError.getMessage())
                        .insertLineBreak()
                        .insertLineBreak();
            }
        }
    }

    @Override public String getReportDirectory() {
        return "build/outputs/checkstyle/";
    }

    /**
     * Returns the path to the report file
     * @return
     */
    @Override
    public FilenameFilter getReportFilenameFilter() {
        return new FilenameFilter() {
            @Override public boolean accept(java.io.File dir, String name) {
                return name.equals("checkstyle.xml");
            }
        };
    }

    /**
     * Returns the class of the report type
     * @return
     */
    @Override
    public Class getReportType() {
        return Checkstyle.class;
    }

    /**
     * Returns the reporter name
     * @return
     */
    @Override
    public String reporterName() {
        return "Checkstyle Reporter";
    }
}
