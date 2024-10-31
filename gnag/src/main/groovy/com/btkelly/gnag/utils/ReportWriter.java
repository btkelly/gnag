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

import com.btkelly.gnag.models.Violation;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 4/18/16.
 */
public final class ReportWriter {

  public static final String REPORT_FILE_NAME = "gnag.html";
  private static final String CSS_FILE_NAME = "github-markdown.css";

  private static final String HTML_REPORT_PREFIX =
      "<!DOCTYPE html>"
          + "<html>"
          + "<link rel=\"stylesheet\" href=\"github-markdown.css\">"
          + "<article class=\"markdown-body\">";

  private static final String HTML_REPORT_SUFFIX = "</article></html>";

  private ReportWriter() {
    // This constructor intentionally left blank.
  }

  public static void writeLocalReportFiles(
      @NotNull final Set<Violation> violations,
      @NotNull final File directory,
      @NotNull final Logger logger) {

    if (violations.isEmpty()) {
      System.err
          .println("writeLocalReportFiles should only be called when violations were detected!");
      return;
    }

    //noinspection ResultOfMethodCallIgnored
    directory.mkdirs();

    final StringBuilder builder = new StringBuilder()
        .append(HTML_REPORT_PREFIX)
        .append(ViolationsFormatter.getHtmlStringForAggregatedComment(violations))
        .append(HTML_REPORT_SUFFIX);

    try {
      final File htmlReportFile = new File(directory, REPORT_FILE_NAME);
      FileUtils.write(htmlReportFile, builder.toString());
    } catch (final IOException e) {
      logger.error("Error writing local Gnag report.", e);
      return;
    }

    copyCssFileToDirectory(directory, logger);
  }

  public static void deleteLocalReportFiles(@NotNull final File directory,
      @NotNull final Logger logger) {
    final File htmlReportFile = new File(directory, REPORT_FILE_NAME);

    if (htmlReportFile.exists()) {
      try {
        FileUtils.forceDelete(htmlReportFile);
      } catch (final IOException e) {
        logger.error("Error deleting local Gnag report.", e);
      }
    }

    deleteCssFileFromDirectory(directory, logger);
  }

  private static void copyCssFileToDirectory(@NotNull final File directory,
      @NotNull final Logger logger) {
    try {
      //TODO fix this
      final InputStream resourceAsStream = ReportWriter.class.getClassLoader()
          .getResourceAsStream(CSS_FILE_NAME);
      final File gnagCssOutputFile = new File(directory, CSS_FILE_NAME);
      FileUtils.copyInputStreamToFile(resourceAsStream, gnagCssOutputFile);
    } catch (final Exception e) {
      logger.error("Error copying CSS file for local Gnag report.", e);
    }
  }

  private static void deleteCssFileFromDirectory(@NotNull final File directory,
      @NotNull final Logger logger) {
    final File gnagCssOutputFile = new File(directory, CSS_FILE_NAME);

    if (gnagCssOutputFile.exists()) {
      try {
        FileUtils.forceDelete(gnagCssOutputFile);
      } catch (final IOException e) {
        logger.error("Error deleting CSS file for local Gnag report.");
      }
    }
  }

}
