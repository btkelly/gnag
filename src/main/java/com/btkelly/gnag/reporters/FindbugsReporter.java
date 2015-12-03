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
import com.btkelly.gnag.utils.Logger;
import org.gradle.api.Project;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class FindbugsReporter implements CommentReporter {

    @Override
    public boolean shouldFailBuild(Project project) {
        try {
            return getFindBugsReport(project).shouldFailBuild();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String textToAppendComment(Project project) {

        Logger.log("Parsing Findbugs violations");

        StringBuilder stringBuilder = new StringBuilder();

        try {
            BugCollection bugCollection = getFindBugsReport(project);

            if (bugCollection.shouldFailBuild()) {

                stringBuilder.append("Findbugs Violations:");
                stringBuilder.append("\n----------------------------------\n");

                for (BugInstance bugInstance : bugCollection.getBugInstance()) {

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

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        Logger.log("Finished parsing Findbugs violations");

        return stringBuilder.toString();
    }

    @Override
    public String reporterName() {
        return "FindBugs Reporter";
    }

    private BugCollection getFindBugsReport(Project project) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(BugCollection.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement<BugCollection> jaxbElement = (JAXBElement<BugCollection>) unmarshaller.unmarshal(getFindbugsReportFile(project));
        return jaxbElement.getValue();
    }

    private java.io.File getFindbugsReportFile(Project project) {
        return new java.io.File(project.getProjectDir(), "/build/outputs/findbugs/findbugs.xml");
    }
}
