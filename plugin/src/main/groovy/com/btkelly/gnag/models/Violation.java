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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private final String type;

    @NotNull
    private final String reporterName;

    @Nullable
    private final String comment;

    @Nullable
    private final String relativeFilePath;

    @Nullable
    private final Integer fileLineNumber;

    @Nullable
    private final String typeUrl;
    
    @NotNull
    private final List<String> secondaryUrls;

    public Violation(
            @NotNull final String type,
            @NotNull final String reporterName,
            @Nullable final String comment,
            @Nullable final String relativeFilePath,
            @Nullable final Integer fileLineNumber) {

        this(type, reporterName, comment, relativeFilePath, fileLineNumber, null);
    }

    /**
     * @param typeUrl a URL identifying a resource that provides more information on this violation's type; null if no
     *                such resource is known
     */
    public Violation(
            @NotNull final String type,
            @NotNull final String reporterName,
            @Nullable final String comment,
            @Nullable final String relativeFilePath,
            @Nullable final Integer fileLineNumber,
            @Nullable final String typeUrl) {

        this(type, reporterName, comment, relativeFilePath, fileLineNumber, typeUrl, new ArrayList<>());
    }

    /**
     * @param typeUrl a URL identifying a resource that provides more information on this violation's type; null if no
     *                such resource is known
     */
    public Violation(
            @NotNull final String type,
            @NotNull final String reporterName,
            @Nullable final String comment,
            @Nullable final String relativeFilePath,
            @Nullable final Integer fileLineNumber,
            @Nullable final String typeUrl,
            @NotNull final List<String> secondaryUrls) {

        this.type = type;
        this.reporterName = reporterName;
        this.comment = comment;
        this.relativeFilePath = relativeFilePath;

        // Treat a line number of 0 as equivalent to a missing line number.
        if (fileLineNumber != null && fileLineNumber == 0) {
            this.fileLineNumber = null;
        } else {
            this.fileLineNumber = fileLineNumber;
        }

        this.typeUrl = typeUrl;
        this.secondaryUrls = secondaryUrls;
    }

    @NotNull
    public String getType() {
        return type;
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
    public String getTypeUrl() {
        return typeUrl;
    }

    @NotNull
    public List<String> getSecondaryUrls() {
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

        if (!type.equals(violation.type)) return false;
        if (!reporterName.equals(violation.reporterName)) return false;
        if (comment != null ? !comment.equals(violation.comment) : violation.comment != null) return false;
        if (relativeFilePath != null ? !relativeFilePath.equals(violation.relativeFilePath) : violation.relativeFilePath != null)
            return false;
        if (fileLineNumber != null ? !fileLineNumber.equals(violation.fileLineNumber) : violation.fileLineNumber != null)
            return false;
        if (typeUrl != null ? !typeUrl.equals(violation.typeUrl) : violation.typeUrl != null) return false;
        return secondaryUrls.equals(violation.secondaryUrls);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + reporterName.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (relativeFilePath != null ? relativeFilePath.hashCode() : 0);
        result = 31 * result + (fileLineNumber != null ? fileLineNumber.hashCode() : 0);
        result = 31 * result + (typeUrl != null ? typeUrl.hashCode() : 0);
        result = 31 * result + secondaryUrls.hashCode();
        return result;
    }
    
}
