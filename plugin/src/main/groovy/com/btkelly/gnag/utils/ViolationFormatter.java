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
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Creates formatted HTML strings representing individual violations.
 */
public final class ViolationFormatter {

  private ViolationFormatter() {
    // This constructor intentionally left blank.
  }

  /**
   * Use this method to create a formatted HTML string representing a violation that will be posted as an inline
   * comment. Only intended for use when posting inline comments to GitHub.
   */
  public static String getHtmlStringForInlineComment(@NotNull final Violation violation) {
    final Integer violationFileLineNumber = violation.getFileLineNumber();
    final String violationRelativeFilePath = violation.getRelativeFilePath();

    if (violationFileLineNumber == null || violationRelativeFilePath == null) {
      throw new IllegalStateException(
          "Should only call getHtmlStringForInlineViolationComment when violation location is known");
    }

    final HtmlStringBuilder builder = new HtmlStringBuilder();

    appendViolationReporterToBuilder(violation, builder);
    appendViolationNameToBuilder(violation, builder);
    appendViolationCommentToBuilderIfPresent(violation, builder);
    appendSecondaryUrlsToBuilderIfPresent(violation, builder);

    return builder.toString();
  }

  /**
   * Use this method to create a formatted HTML string representing a violation that will be posted as part of an
   * aggregated comment. Intended for use when generating the local Gnag report, and when posting aggregated comments
   * to GitHub.
   */
  public static String getHtmlStringForAggregatedComment(@NotNull final Violation violation) {
    final HtmlStringBuilder builder = new HtmlStringBuilder();

    appendViolationNameToBuilder(violation, builder);
    appendLocationInformationToBuilderIfPresent(violation, builder);
    appendViolationCommentToBuilderIfPresent(violation, builder);
    appendSecondaryUrlsToBuilderIfPresent(violation, builder);

    return builder.toString();
  }

  private static void appendViolationReporterToBuilder(
      @NotNull final Violation violation,
      @NotNull final HtmlStringBuilder builder) {

    builder.appendBold("Reporter: ");
    builder.append(violation.getReporterName());
    builder.insertLineBreak();
  }

  private static void appendViolationNameToBuilder(
      @NotNull final Violation violation,
      @NotNull final HtmlStringBuilder builder) {

    builder.appendBold("Violation: ");

    final String violationType = violation.getType();
    final String violationTypeUrl = violation.getTypeUrl();

    if (StringUtils.isNotBlank(violationTypeUrl)) {
      builder.appendLink(violationType, violationTypeUrl);
    } else {
      builder.append(violationType);
    }
  }

  @SuppressWarnings("ConstantConditions")
  private static void appendLocationInformationToBuilderIfPresent(
      @NotNull final Violation violation,
      @NotNull final HtmlStringBuilder builder) {

    if (violation.hasAllLocationInfo()) {
      builder
          .insertLineBreak()
          .appendBold("Location: ")
          .appendCode(String
              .format("%s:%s", violation.getRelativeFilePath(), violation.getFileLineNumber()));
    } else if (violation.hasRelativeFilePath()) {
      builder
          .insertLineBreak()
          .appendBold("File: ")
          .appendCode(violation.getRelativeFilePath());
    }
  }

  private static void appendViolationCommentToBuilderIfPresent(
      @NotNull final Violation violation,
      @NotNull final HtmlStringBuilder builder) {

    final String violationComment = violation.getComment();

    if (violationComment != null) {
      builder
          .insertLineBreak()
          .appendBold("Notes: ")
          .append(violationComment);
    }
  }

  private static void appendSecondaryUrlsToBuilderIfPresent(
      @NotNull final Violation violation,
      @NotNull final HtmlStringBuilder builder) {

    final List<String> secondaryViolationUrls = violation.getSecondaryUrls();
    final int numberOfSecondaryViolationUrls = secondaryViolationUrls.size();

    if (numberOfSecondaryViolationUrls > 0) {
      builder
          .insertLineBreak()
          .appendBold("Related: ");

      for (int urlNumber = 0; urlNumber < numberOfSecondaryViolationUrls; urlNumber++) {
        builder.appendLink(secondaryViolationUrls.get(urlNumber),
            secondaryViolationUrls.get(urlNumber));

        if (urlNumber != numberOfSecondaryViolationUrls - 1) {
          builder.append(", ");
        }
      }
    }
  }

}
