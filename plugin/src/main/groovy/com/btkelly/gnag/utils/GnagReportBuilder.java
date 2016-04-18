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
            "</article></head></html>";

    private final File outputDirectory;

    public GnagReportBuilder(@NotNull File outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.outputDirectory.mkdirs();
        append(HTML_REPORT_PREFIX);
        copyCssFile();
    }

    public boolean writeFile() {

        final File htmlReportFile = new File(outputDirectory, REPORT_FILE_NAME);
        String contents = toString();
        contents += HTML_REPORT_SUFFIX;

        try {
            FileUtils.write(htmlReportFile, contents);
        } catch (IOException e) {
            System.out.println("Error writing Gnag local report.");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void copyCssFile() {
        try {
            //TODO fix this
            final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(CSS_FILE_NAME);
            final File gnagCssOutputFile = new File(outputDirectory, CSS_FILE_NAME);
            FileUtils.copyInputStreamToFile(resourceAsStream, gnagCssOutputFile);

        } catch (final Exception e) {
            e.printStackTrace();
            System.out.println("Error copying CSS file for local report.");
        }
    }
}
