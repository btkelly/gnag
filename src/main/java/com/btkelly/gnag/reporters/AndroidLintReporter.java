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

import com.btkelly.gnag.models.android_lint.Issue;
import com.btkelly.gnag.models.android_lint.Issues;
import com.btkelly.gnag.utils.HtmlStringBuilder;

import java.io.File;
import java.io.FilenameFilter;

public class AndroidLintReporter extends BaseReporter<Issues> {

  @Override
  public void appendViolationText(Issues report, String projectDir, HtmlStringBuilder htmlStringBuilder) {
    System.out.println(report.getIssue().size());

    for (Issue issue : report.getErrors()) {
      final String fullFileName = issue.getLocation().getFile();

      // FIXME: handle trailing slashes here in case we get directory names:
      final String shortFileName
          = fullFileName.substring(fullFileName.lastIndexOf(File.separator) + 1);

      htmlStringBuilder
              .appendBold("Violation: ")
              .append(issue.getId())
              .insertLineBreak()
              .appendBold("File: ")
              .append(shortFileName)
              .insertLineBreak()
              .appendBold("Line: ")
              .append(issue.getLocation().getLine())
              .insertLineBreak()
              .appendBold("Notes: ")
              .append(issue.getMessage())
              .insertLineBreak()
              .insertLineBreak();
    }
  }

  @Override
  public String getReportDirectory() {
    return "build/outputs/";
  }

  @Override
  public FilenameFilter getReportFilenameFilter() {
    return new FilenameFilter() {
      @Override public boolean accept(File dir, String name) {
        return name.startsWith("lint-results-") && name.endsWith(".xml");
      }
    };
  }

  @Override
  public Class getReportType() {
    return Issues.class;
  }

  @Override
  public String reporterName() {
    return "Android Lint Reporter";
  }

}
