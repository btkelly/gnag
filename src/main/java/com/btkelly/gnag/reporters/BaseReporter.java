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

import com.btkelly.gnag.models.Report;
import com.btkelly.gnag.utils.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.gradle.api.Nullable;
import org.gradle.api.Project;

/**
 * Created by bobbake4 on 12/3/15.
 */
public abstract class BaseReporter<T extends Report> implements CommentReporter {

    public abstract void appendViolationText(T report, String projectDir, StringBuilder stringBuilder);
    public abstract String getReportDirectory();
    public abstract FilenameFilter getReportFilenameFilter();
    public abstract Class getReportType();

    /**
     * Looks through the report and determines if the build should fail
     * @param project - current project being built
     * @return - true if the build should fail
     */
    @Override
    public final boolean shouldFailBuild(Project project) {
        try {
            return getReport(project).shouldFailBuild();
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Asks derived classes to append text to the comment based on report results
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    @Override
    public final String textToAppendComment(Project project) {

        Logger.logInfo("Parsing " + reporterName() + " violations");

        StringBuilder stringBuilder = new StringBuilder();

        try {
            T report = getReport(project);

            if (report.shouldFailBuild()) {

                stringBuilder.append(reporterName() + " Violations:");
                stringBuilder.append("\n----------------------------------\n");

                String projectDir = project.getProjectDir().toString();
                appendViolationText(report, projectDir, stringBuilder);
            }
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        Logger.logInfo("Finished parsing " + reporterName() + " violations");

        return stringBuilder.toString();
    }

    private T getReport(Project project) throws JAXBException, FileNotFoundException {
        JAXBContext jaxbContext = JAXBContext.newInstance(getReportType());
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Object rawReport = unmarshaller.unmarshal(getReportFile(project));

        if (rawReport instanceof JAXBElement) {
            JAXBElement<T> jaxbElement = (JAXBElement<T>) rawReport;
            return jaxbElement.getValue();
        } else {
            return (T) rawReport;
        }
    }

    @Nullable
    private java.io.File getReportFile(Project project) throws FileNotFoundException {
        final File searchDirectory = new File(project.getProjectDir(), getReportDirectory());

        final java.io.File[] matchingFiles = searchDirectory.listFiles(getReportFilenameFilter());

        if (matchingFiles.length == 0) {
            throw new FileNotFoundException("Could not locate any report file using filter "
                + getReportFilenameFilter().toString());
        } else if (matchingFiles.length >= 2) {
            throw new FileNotFoundException("Could not locate unique report file using filter "
                + getReportFilenameFilter().toString());
        } else {
            return matchingFiles[0];
        }

    }

}
