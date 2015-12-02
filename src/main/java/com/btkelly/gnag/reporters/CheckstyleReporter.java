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

import com.btkelly.gnag.models.checkstyle.Checkstyle;
import com.btkelly.gnag.models.checkstyle.Error;
import com.btkelly.gnag.models.checkstyle.File;
import org.gradle.api.Project;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Comment reporter for Checkstyle. Looks for Checkstyle report in the default location "/build/outputs/checkstyle/checkstyle.xml"
 */
public class CheckstyleReporter implements CommentReporter {

    /**
     * Looks through the Checkstyle report and determines if any file node has children.
     * @param project - current project being built
     * @return - true if any file node has children
     */
    @Override
    public boolean shouldFailBuild(Project project) {
        try {
            return getCheckstyleReport(project).shouldFailBuild();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Loops through all Checkstyle errors and pulls out file name, line number, error message, and rule
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    @Override
    public String textToAppendComment(Project project) {

        //println"Parsing Checkstyle violations";

        StringBuilder stringBuilder = new StringBuilder();

        try {
            Checkstyle checkstyleReport = getCheckstyleReport(project);

            if (checkstyleReport.shouldFailBuild()) {

                stringBuilder.append("Checkstyle Violations:");
                stringBuilder.append("\n----------------------------------\n");

                String projectDir = project.getProjectDir().toString();

                for (File checkstyleFile : checkstyleReport.getFile()) {

                    String fileName = checkstyleFile.getName();
                    fileName = fileName.replace(projectDir, "");
                    fileName = fileName.replace("/src/main/java/", "");
                    fileName = fileName.replace("/", ".");

                    for (Error checkstyleError : checkstyleFile.getError()) {

                        stringBuilder.append("<b>Violation: </b> " + checkstyleError.getSource());
                        stringBuilder.append("\n");
                        stringBuilder.append("<b>Class: </b>" + fileName);
                        stringBuilder.append(" - ");
                        stringBuilder.append(" <b>Line: </b>" + checkstyleError.getLine());
                        stringBuilder.append("\n");
                        stringBuilder.append(checkstyleError.getMessage());
                        stringBuilder.append("\n\n");
                    }
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //println "Finished parsing Checkstyle violations";

        return stringBuilder.toString();
    }

    @Override
    public String reporterName() {
        return "Checkstyle Reporter";
    }

    private Checkstyle getCheckstyleReport(Project project) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Checkstyle.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return (Checkstyle) unmarshaller.unmarshal(getCheckstyleReportFile(project));
    }

    private java.io.File getCheckstyleReportFile(Project project) {
        return new java.io.File(project.getProjectDir(), "/build/outputs/checkstyle/checkstyle.xml");
    }
}
