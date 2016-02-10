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
package com.btkelly.gnag.reporters;

import com.btkelly.gnag.models.findbugs.BugCollection;
import com.btkelly.gnag.models.findbugs.BugInstance;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Comment reporter for Findbugs. Looks for Findbugs report in the default location "/build/outputs/findbugs/findbugs.xml"
 */
public class FindbugsReporter extends BaseReporter<BugCollection> {

    /**
     * Loops through all Findbugs errors and pulls out file name, line number, error message, and rule
     * @param report - parsed report object
     * @param projectDir - base project directory
     * @param stringBuilder - StringBuilder to append the comment to
     */
    @Override
    public void appendViolationText(BugCollection report, String projectDir, StringBuilder stringBuilder) {

        for (BugInstance bugInstance : report.getBugInstance()) {

            stringBuilder.append("<b>Violation: </b> " + bugInstance.getType());
            stringBuilder.append("\n");
            stringBuilder.append("<b>Class: </b>" + bugInstance.getSourceLine().getClassname());
            stringBuilder.append(" - ");
            stringBuilder.append(" <b>Line: </b>" + bugInstance.getSourceLine().getStart());
            stringBuilder.append("\n");
            stringBuilder.append(bugInstance.getShortMessage());
            stringBuilder.append("\n\n");
        }
    }

    @Override public String getReportDirectory() {
        return "build/outputs/findbugs/";
    }

    /**
     * Returns the path to the report file
     * @return
     */
    @Override
    public FilenameFilter getReportFilenameFilter() {
        return new FilenameFilter() {
            @Override public boolean accept(File dir, String name) {
                return name.equals("findbugs.xml");
            }
        };
    }

    /**
     * Returns the class of the report type
     * @return
     */
    @Override
    public Class getReportType() {
        return BugCollection.class;
    }

    /**
     * Returns the reporter name
     * @return
     */
    @Override
    public String reporterName() {
        return "FindBugs Reporter";
    }
}
