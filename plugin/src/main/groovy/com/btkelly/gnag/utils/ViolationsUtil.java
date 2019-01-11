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

import com.btkelly.gnag.models.PRLocation;
import com.btkelly.gnag.models.Violation;
import com.github.stkent.githubdiffparser.models.Diff;
import com.github.stkent.githubdiffparser.models.Hunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ViolationsUtil {

    public static Set<Violation> hasViolationWithAllLocationInformation(@NotNull final Set<Violation> violations) {
        return violations.stream()
                .filter(Violation::hasAllLocationInfo)
                .collect(Collectors.toSet());
    }

    @NotNull
    public static Map<Violation, PRLocation> getPRLocationsForViolations(
            @NotNull final Set<Violation> violations,
            @NotNull final List<Diff> diffs) {

        final Map<Violation, PRLocation> result = new HashMap<>();

        violations.forEach(violation ->
                result.put(violation, getPRLocationForViolation(violation, diffs))
        );

        return result;
    }

    /**
     * @return a PRLocation if the given violation can be located within exactly one of the given diffs; null otherwise
     */
    @Nullable
    private static PRLocation getPRLocationForViolation(
            @NotNull final Violation violation,
            @NotNull final List<Diff> diffs) {

        if (!violation.hasAllLocationInfo()) {
            return null;
        }

        final String violationRelativeFilePath = violation.getRelativeFilePath();

        //noinspection ConstantConditions
        final List<Diff> diffsMatchingViolationFilePath = diffs.stream()
                .filter(diff -> violationRelativeFilePath.equals(diff.getToFileName()))
                .collect(Collectors.toList());

        if (diffsMatchingViolationFilePath.size() == 1) {
            return getPRLocationForViolation(violation, diffsMatchingViolationFilePath.get(0));

        } else {
            return null;
        }
    }

    /**
     * @return a PRLocation if the given violation can be located within the given diff; null otherwise
     */
    @Nullable
    private static PRLocation getPRLocationForViolation(
            @NotNull final Violation violation,
            @NotNull final Diff diffMatchingViolationFilePath) {

        if (!violation.hasAllLocationInfo()) {
            return null;
        }

        final String violationRelativeFilePath = violation.getRelativeFilePath();

        //noinspection ConstantConditions
        final Integer rawDiffLineNumberForToFileLocation =
                diffMatchingViolationFilePath.getDiffLineNumberForToFileLocation(
                        violationRelativeFilePath, violation.getFileLineNumber());

        if (rawDiffLineNumberForToFileLocation != null) {
            final int offsetDiffLineNumberForToFileLocation
                    = rawDiffLineNumberForToFileLocation
                            - Diff.NUMBER_OF_LINES_PER_DELIMITER
                            - diffMatchingViolationFilePath.getHeaderLines().size()
                            - Hunk.NUMBER_OF_LINES_PER_DELIMITER;

            return new PRLocation(violationRelativeFilePath, offsetDiffLineNumberForToFileLocation);
        } else {
            return null;
        }
    }

    private ViolationsUtil() {
        // This constructor intentionally left blank.
    }

}
