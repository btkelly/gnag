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

import com.btkelly.gnag.models.pmd.File;
import com.btkelly.gnag.models.pmd.Pmd;
import com.btkelly.gnag.models.pmd.Violation;
import com.btkelly.gnag.utils.HtmlStringBuilder;

import java.io.FilenameFilter;

/**
 * Comment reporter for PMD. Looks for PMD report in the default location "/build/outputs/pmd/pmd.xml"
 */
public class PMDReporter extends BaseReporter<Pmd> {

    /**
     * Loops through all PMD errors and pulls out file name, line number, error message, help url, and rule
     * @param report - parsed report object
     * @param projectDir - base project directory
     * @param htmlStringBuilder - StringBuilder to append the comment to
     */
    @Override
    public void appendViolationText(Pmd report, String projectDir, HtmlStringBuilder htmlStringBuilder) {

        for (File file : report.getFile()) {

            String fileName = file.getName();
            fileName = fileName.replace(projectDir, "");
            fileName = fileName.replace("/src/main/java/", "");
            fileName = fileName.replace("/", ".");

            for (Violation violation : file.getViolation()) {
                final String helpUrl = violation.getExternalInfoUrl();

                htmlStringBuilder
                        .appendBold("Violation: ")
                        .appendLink(violation.getRule(), helpUrl)
                        .insertLineBreak()
                        .appendBold("Class: ")
                        .append(fileName)
                        .insertLineBreak()
                        .appendBold("Line: ")
                        .append(violation.getBeginline())
                        .insertLineBreak()
                        .appendBold("Notes: ")
                        .append(violation.getValue())
                        .insertLineBreak()
                        .insertLineBreak();
            }
        }
    }

    @Override public String getReportDirectory() {
        return "build/outputs/pmd/";
    }

    /**
     * Returns the path to the report file
     * @return
     */
    @Override
    public FilenameFilter getReportFilenameFilter() {
        return new FilenameFilter() {
            @Override public boolean accept(java.io.File dir, String name) {
                return name.equals("pmd.xml");
            }
        };
    }

    /**
     * Returns the class of the report type
     * @return
     */
    @Override
    public Class getReportType() {
        return Pmd.class;
    }

    /**
     * Returns the reporter name
     * @return
     */
    @Override
    public String reporterName() {
        return "PMD Reporter";
    }
}
