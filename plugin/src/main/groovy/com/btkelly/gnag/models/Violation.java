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
package com.btkelly.gnag.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;

public final class Violation {

    /**
     * WARNING: do not use this comparator to sort Violations with missing or partial location information only!
     *
     * Violations that will be reported as inline comments should be reported in the order specified by this Comparator.
     * Comments will be sorted first by relative file path, then within each file by line number.
     */
    @SuppressWarnings("ConstantConditions")
    public static final Comparator<Violation> COMMENT_POSTING_COMPARATOR = (v1, v2) -> {
        if (!v1.hasAllLocationInfo() || !v2.hasAllLocationInfo()) {
            return 0;
        }

        final String v1RelativeFilePath = v1.getRelativeFilePath();
        final String v2RelativeFilePath = v2.getRelativeFilePath();

        if (v1RelativeFilePath.equals(v2RelativeFilePath)) {
            return v1.getFileLineNumber().compareTo(v2.getFileLineNumber());
        } else {
            return v1RelativeFilePath.compareTo(v2RelativeFilePath);
        }
    };

    @NotNull
    private final String name;

    @NotNull
    private final String reporterName;

    @Nullable
    private final String comment;

    @Nullable
    private final String relativeFilePath;

    @Nullable
    private final Integer fileLineNumber;

    @Nullable
    private final String primaryUrl;
    
    @NotNull
    private final String[] secondaryUrls;

    public Violation(
            @NotNull final String name,
            @NotNull final String reporterName,
            @Nullable final String comment,
            @Nullable final String relativeFilePath,
            @Nullable final Integer fileLineNumber,
            @Nullable final String primaryUrl) {

        this(name, reporterName, comment, relativeFilePath, fileLineNumber, primaryUrl, new String[]{});
    }

    public Violation(
            @NotNull final String name,
            @NotNull final String reporterName,
            @Nullable final String comment,
            @Nullable final String relativeFilePath,
            @Nullable final Integer fileLineNumber,
            @Nullable final String primaryUrl,
            @NotNull final String[] secondaryUrls) {

        this.name = name;
        this.reporterName = reporterName;
        this.comment = comment;
        this.relativeFilePath = relativeFilePath;

        // Treat a line number of 0 as equivalent to a missing line number.
        if (fileLineNumber != null && fileLineNumber == 0) {
            this.fileLineNumber = null;
        } else {
            this.fileLineNumber = fileLineNumber;
        }

        this.primaryUrl = primaryUrl;
        this.secondaryUrls = secondaryUrls;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getReporterName() {
        return reporterName;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    @Nullable
    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    @Nullable
    public Integer getFileLineNumber() {
        return fileLineNumber;
    }

    @Nullable
    public String getPrimaryUrl() {
        return primaryUrl;
    }

    @NotNull
    public String[] getSecondaryUrls() {
        return secondaryUrls;
    }

    public boolean hasAllLocationInfo() {
        return getRelativeFilePath() != null && getFileLineNumber() != null;
    }

    // Generated equals and hashcode

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Violation violation = (Violation) o;

        if (!name.equals(violation.name)) return false;
        if (!reporterName.equals(violation.reporterName)) return false;
        if (comment != null ? !comment.equals(violation.comment) : violation.comment != null) return false;
        if (relativeFilePath != null ? !relativeFilePath.equals(violation.relativeFilePath) : violation.relativeFilePath != null)
            return false;
        if (fileLineNumber != null ? !fileLineNumber.equals(violation.fileLineNumber) : violation.fileLineNumber != null)
            return false;
        if (primaryUrl != null ? !primaryUrl.equals(violation.primaryUrl) : violation.primaryUrl != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(secondaryUrls, violation.secondaryUrls);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + reporterName.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (relativeFilePath != null ? relativeFilePath.hashCode() : 0);
        result = 31 * result + (fileLineNumber != null ? fileLineNumber.hashCode() : 0);
        result = 31 * result + (primaryUrl != null ? primaryUrl.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(secondaryUrls);
        return result;
    }
    
}
