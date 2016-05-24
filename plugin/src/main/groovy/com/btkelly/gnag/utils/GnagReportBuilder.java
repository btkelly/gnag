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
package com.btkelly.gnag.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bobbake4 on 4/18/16.
 */
public class GnagReportBuilder extends HtmlStringBuilder {

    private static final String REPORT_FILE_NAME = "gnag.html";
    private static final String CSS_FILE_NAME = "github-markdown.css";

    private static final String HTML_REPORT_PREFIX =
            "<!DOCTYPE html>"
                    + "<html>"
                    + "<link rel=\"stylesheet\" href=\"github-markdown.css\">"
                    + "<article class=\"markdown-body\">";

    private static final String HTML_REPORT_SUFFIX =
            "</article></html>";

    private final Project project;

    public GnagReportBuilder(@NotNull Project project) {
        this.project = project;
    }

    public GnagReportBuilder insertReporterHeader(String reporterName) {
        return (GnagReportBuilder) append("<h2>")
                .append(reporterName + " Violations:")
                .append("</h2>");
    }

    public GnagReportBuilder appendViolation(String name, String helpUrl, String fileName, String lineNumber, String notes) {

        fileName = fileName.replace(project.getProjectDir().toString(), "");
        fileName = fileName.replace("/src/main/java/", "");
        fileName = fileName.replace("/src/test/java/", "");
        fileName = fileName.replace("/src/androidTest/java/", "");
        fileName = fileName.replace("/src/main/", "");
        fileName = fileName.replace("/", ".");

        appendBold("Violation: ");

        if (StringUtils.isNotBlank(helpUrl)) {
            appendLink(name, helpUrl);
        } else {
            append(name);
        }

        notes = notes.replaceAll("\\r|\\n", "");

        return (GnagReportBuilder) insertLineBreak()
                .appendBold("File: ")
                .append(fileName)
                .insertLineBreak()
                .appendBold("Line: ")
                .append(lineNumber)
                .insertLineBreak()
                .appendBold("Notes: ")
                .append(notes)
                .insertLineBreak()
                .insertLineBreak();
    }

    public boolean writeReportToDirectory(@NotNull final File directory) {
        //noinspection ResultOfMethodCallIgnored
        directory.mkdirs();

        StringBuilder fullReport = new StringBuilder();

        fullReport.append(HTML_REPORT_PREFIX);
        fullReport.append(toString());
        fullReport.append(HTML_REPORT_SUFFIX);

        try {
            File htmlReportFile = new File(directory, REPORT_FILE_NAME);
            FileUtils.write(htmlReportFile, fullReport.toString());
        } catch (IOException e) {
            System.out.println("Error writing Gnag local report.");
            e.printStackTrace();
            return false;
        }

        copyCssFileToDirectory(directory);

        return true;
    }

    private void copyCssFileToDirectory(@NotNull final File directory) {
        try {
            //TODO fix this
            final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(CSS_FILE_NAME);
            final File gnagCssOutputFile = new File(directory, CSS_FILE_NAME);
            FileUtils.copyInputStreamToFile(resourceAsStream, gnagCssOutputFile);

        } catch (final Exception e) {
            System.out.println("Error copying CSS file for local report.");
        }
    }
}
