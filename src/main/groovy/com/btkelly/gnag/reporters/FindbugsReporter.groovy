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
 * Created by bobbake4 on 11/5/15.
 */
class FindbugsReporter implements CommentReporter {

    @Override
    boolean shouldFailBuild(Project project) {
        Node findBugsXMLTree = getFindbugsXMLTree(project);
        NodeList bugs = findBugsXMLTree.get("BugInstance") as NodeList
        return bugs.size() != 0;
    }

    @Override
    String textToAppendComment(Project project) {

        println "Parsing Findbugs violations";

        Node findBugsXMLTree = getFindbugsXMLTree(project);
        NodeList bugs = findBugsXMLTree.get("BugInstance") as NodeList

        StringBuilder stringBuilder = new StringBuilder();

        if (bugs.size() > 0 ) {

            stringBuilder.append("Findbugs Violations:")
            stringBuilder.append("\n----------------------------------\n");

            bugs.each { bug ->

                String lineNumber = XMLUtil.cleanseXMLString((String) bug.SourceLine.@start);
                String violationText = XMLUtil.cleanseXMLString((String) bug.ShortMessage.text());
                String violationRule = XMLUtil.cleanseXMLString((String) bug.@type);
                String fileName = XMLUtil.cleanseXMLString((String) bug.SourceLine.@classname);

                stringBuilder.append("<b>Violation: </b> " + violationRule);
                stringBuilder.append("\n");
                stringBuilder.append("<b>Class: </b>" + fileName);
                stringBuilder.append(" - ");
                stringBuilder.append(" <b>Line: </b>" + lineNumber);
                stringBuilder.append("\n");
                stringBuilder.append(violationText);
                stringBuilder.append("\n\n");
            }
        }

        println "Finished parsing Findbugs violations";

        return stringBuilder.toString();
    }

    @Override
    String reporterName() {
        return "FindBugs Reporter";
    }

    private Node getFindbugsXMLTree(Project project) {
        File findbugsFile = getFindbugsReportFile(project);
        return new XmlParser().parse(findbugsFile);
    }

    private File getFindbugsReportFile(Project project) {
        return new File(project.projectDir, "/build/outputs/findbugs/findbugs.xml");
    }
}
