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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Creates formatted HTML strings representing individual violations.
 */
public final class ViolationFormatter {

    private static final String VIOLATION_FILE_LABEL  = "File: ";
    private static final String VIOLATION_LINE_LABEL  = "Line: ";
    private static final String VIOLATION_NAME_LABEL  = "Violation: ";
    private static final String VIOLATION_NOTES_LABEL = "Notes: ";
    private static final String VIOLATION_REPORTER_LABEL = "Reporter: ";

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

        return builder.toString();
    }

    private static void appendViolationReporterToBuilder(
            @NotNull final Violation violation,
            @NotNull final HtmlStringBuilder builder) {

        builder.appendBold(VIOLATION_REPORTER_LABEL);
        builder.append(violation.getReporterName());
        builder.insertLineBreak();
    }

    private static void appendViolationNameToBuilder(
            @NotNull final Violation violation,
            @NotNull final HtmlStringBuilder builder) {
        
        builder.appendBold(VIOLATION_NAME_LABEL);
        
        final String violationName = violation.getName();
        final String violationUrl = violation.getUrl();

        if (StringUtils.isNotBlank(violationUrl)) {
            builder.appendLink(violationName, violationUrl);
        } else {
            builder.append(violationName);
        }
    }
    
    private static void appendLocationInformationToBuilderIfPresent(
            @NotNull final Violation violation,
            @NotNull final HtmlStringBuilder builder) {

        final String violationRelativeFilePath = violation.getRelativeFilePath();
        
        if (violationRelativeFilePath != null) {
            appendRelativeFilePathToBuilder(violationRelativeFilePath, builder);

            final Integer violationFileLineNumber = violation.getFileLineNumber();
            
            if (violationFileLineNumber != null) {
                appendViolationLineNumberToBuilder(violationFileLineNumber, builder);
            }
        }
    }
    
    private static void appendRelativeFilePathToBuilder(
            @NotNull final String violationRelativeFilePath,
            @NotNull final HtmlStringBuilder builder) {
        
        builder
                .insertLineBreak()
                .appendBold(VIOLATION_FILE_LABEL)
                .append(violationRelativeFilePath);
    }

    private static void appendViolationLineNumberToBuilder(
            @NotNull final Integer violationFileLineNumber,
            @NotNull final HtmlStringBuilder builder) {
        
        builder
                .insertLineBreak()
                .appendBold(VIOLATION_LINE_LABEL)
                .append(Integer.toString(violationFileLineNumber));
    }

    private static void appendViolationCommentToBuilderIfPresent(
            @NotNull final Violation violation,
            @NotNull final HtmlStringBuilder builder) {

        final String violationComment = violation.getComment();

        if (violationComment != null) {
            builder
                    .insertLineBreak()
                    .appendBold(VIOLATION_NOTES_LABEL)
                    .append(violationComment);
        }
    }

    private ViolationFormatter() {
        
    }
    
}
