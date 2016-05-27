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

public class Violation {
    
    @NotNull
    private final String name;
    
    @NotNull
    private final String reporterName;

    @Nullable
    private final String comment;

    @Nullable
    private final String url;
    
    @Nullable
    private final String relativeFilePath;
    
    @Nullable
    private final Integer fileLineNumber;

    public Violation(
            @NotNull  final String name,
            @NotNull  final String reporterName,
            @Nullable final String comment,
            @Nullable final String url,
            @Nullable final String relativeFilePath,
            @Nullable final Integer fileLineNumber) {
        
        this.name = name;
        this.reporterName = reporterName;
        this.comment = comment;
        this.url = url;
        this.relativeFilePath = relativeFilePath;

        // Treat a line number of 0 as equivalent to a missing line number.
        if (fileLineNumber != null && fileLineNumber == 0) {
            this.fileLineNumber = null;
        } else {
            this.fileLineNumber = fileLineNumber;
        }
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
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    @Nullable
    public Integer getFileLineNumber() {
        return fileLineNumber;
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
        if (url != null ? !url.equals(violation.url) : violation.url != null) return false;
        if (relativeFilePath != null ? !relativeFilePath.equals(violation.relativeFilePath) : violation.relativeFilePath != null)
            return false;
        return fileLineNumber != null ? fileLineNumber.equals(violation.fileLineNumber) : violation.fileLineNumber == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + reporterName.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (relativeFilePath != null ? relativeFilePath.hashCode() : 0);
        result = 31 * result + (fileLineNumber != null ? fileLineNumber.hashCode() : 0);
        return result;
    }
    
}
