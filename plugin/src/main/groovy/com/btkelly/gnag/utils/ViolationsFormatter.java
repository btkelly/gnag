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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Creates formatted HTML strings representing collections of violations.
 */
public final class ViolationsFormatter {

    public static String getHtmlStringForAggregatedComment(@NotNull final Set<Violation> violations) {
        final Map<String, List<Violation>> groupedViolations = violations
                .stream()
                .collect(Collectors.groupingBy(Violation::getReporterName));

        final HtmlStringBuilder builder = new HtmlStringBuilder();

        for (final Map.Entry<String, List<Violation>> entry : groupedViolations.entrySet()) {
            builder
                    .append("<h2>")
                    .append(entry.getKey() + " Violations")
                    .append("</h2>");

            for (final Violation violation : entry.getValue()) {
                builder
                        .append(ViolationFormatter.getHtmlStringForAggregatedComment(violation))
                        .insertLineBreak()
                        .insertLineBreak();
            }
        }

        return builder.toString();
    }

    private ViolationsFormatter() {

    }

}
