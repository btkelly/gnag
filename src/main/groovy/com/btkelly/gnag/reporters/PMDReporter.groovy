/*
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
package com.btkelly.gnag.reporters

import com.btkelly.gnag.utils.XMLUtil
import org.gradle.api.Project

/**
 * Comment reporter for PMD. Looks for PMD report in the default location "/build/outputs/pmd/pmd.xml"
 */
class PMDReporter implements CommentReporter {

    /**
     * Looks through the PMD report and determines if the root has any children.
     * @param project - current project being built
     * @return - true if the root node has children
     */
    @Override
    boolean shouldFailBuild(Project project) {
        Node pmdXMLTree = getPMDXMLTree(project);
        return pmdXMLTree.children().size() != 0;
    }


    /**
     * Loops through all PMD violations and pulls out file name, help url, line number, error message, and rule
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    @Override
    String textToAppendComment(Project project) {

        String projectDir = project.projectDir;

        println "Parsing PMD violations";

        Node pmdXMLTree = getPMDXMLTree(project);

        StringBuilder stringBuilder = new StringBuilder();

        if (pmdXMLTree.children().size() > 0 ) {

            stringBuilder.append("PMD Violations:")
            stringBuilder.append("\n----------------------------------\n");

            pmdXMLTree.file.each { file ->

                String helpURL = XMLUtil.cleanseXMLString((String) file.violation.@externalInfoUrl);
                String lineNumber = XMLUtil.cleanseXMLString((String) file.violation.@beginline);
                String violationText = XMLUtil.cleanseXMLString((String) file.violation.text());
                String violationRule = XMLUtil.cleanseXMLString((String) file.violation.@rule);

                String fileName = file.@name;
                fileName = fileName.replace(projectDir, "");
                fileName = fileName.replace("/src/main/java/", "");
                fileName = fileName.replace("/", ".");

                stringBuilder.append("<b>Violation: </b> " + violationRule);
                stringBuilder.append("\n");
                stringBuilder.append("<b>Help: </b> " + helpURL);
                stringBuilder.append("\n");
                stringBuilder.append("<b>Class: </b>" + fileName);
                stringBuilder.append(" - ");
                stringBuilder.append(" <b>Line: </b>" + lineNumber);
                stringBuilder.append("\n");
                stringBuilder.append(violationText);
                stringBuilder.append("\n\n");
            }
        }

        println "Finished parsing PMD violations";

        return stringBuilder.toString();
    }

    @Override
    String reporterName() {
        return "PMD Reporter"
    }

    private Node getPMDXMLTree(Project project) {
        File pmdFile = getPMDReportFile(project);
        return new XmlParser().parse(pmdFile);
    }

    private File getPMDReportFile(Project project) {
        return new File(project.projectDir, "/build/outputs/pmd/pmd.xml");
    }
}
