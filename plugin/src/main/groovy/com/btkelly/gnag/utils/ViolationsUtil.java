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
import com.github.stkent.githubdiffparser.models.Diff;
import com.github.stkent.githubdiffparser.models.Hunk;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ViolationsUtil {

    public static Integer getOffsetDiffLineNumberForViolation(
            @NotNull final Violation violation, @NotNull final List<Diff> diffs) {

        if (!violation.hasAllLocationInfo()) {
            return null;
        }

        //noinspection ConstantConditions
        final List<Diff> diffsContainingViolationLocation = new ArrayList<>();
        final List<Integer> offsetDiffLineNumbers = new ArrayList<>();
        
        diffs.stream()
                .forEach(diff -> {
                    //noinspection ConstantConditions
                    final Integer diffLineNumberForToFileLocation =
                            diff.getDiffLineNumberForToFileLocation(
                                    violation.getRelativeFilePath(), violation.getFileLineNumber());
                    
                    if (diffLineNumberForToFileLocation != null) {
                        diffsContainingViolationLocation.add(diff);
                        offsetDiffLineNumbers.add(diffLineNumberForToFileLocation);
                    }
                });
        
        if (diffsContainingViolationLocation.size() != 1) {
            return null;
        }

        return offsetDiffLineNumbers.get(0)
                - Diff.NUMBER_OF_LINES_PER_DELIMITER
                - diffsContainingViolationLocation.get(0).getHeaderLines().size()
                - Hunk.NUMBER_OF_LINES_PER_DELIMITER;
    }

    @NotNull
    public static Set<Violation> getViolationsWithAllLocationInfo(@NotNull final Set<Violation> violations) {
        return violations
                .stream()
                .filter(Violation::hasAllLocationInfo)
                .collect(Collectors.toSet());
    }

    @NotNull
    public static Set<Violation> getViolationsWithValidLocationInfo(
            @NotNull final Set<Violation> violations,
            @NotNull final List<Diff> diffs) {

        //noinspection Convert2Lambda
        return getViolationsWithAllLocationInfo(violations)
                .stream()
                .filter(violation -> getOffsetDiffLineNumberForViolation(violation, diffs) != null)
                .collect(Collectors.toSet());
    }
    
    @NotNull
    public static Set<Violation> getViolationsWithMissingOrInvalidLocationInfo(
            @NotNull final Set<Violation> violations,
            @NotNull final List<Diff> diffWrapper) {

        final Set<Violation> result = new HashSet<>(violations);
        result.removeAll(getViolationsWithValidLocationInfo(violations, diffWrapper));
        return result;
    }
    
    private ViolationsUtil() {
        // This constructor intentionally left blank.
    }
    
}
