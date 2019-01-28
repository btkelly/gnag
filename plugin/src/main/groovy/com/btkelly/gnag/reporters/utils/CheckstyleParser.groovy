/*
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
package com.btkelly.gnag.reporters.utils

import com.btkelly.gnag.models.Violation
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project

import static com.btkelly.gnag.utils.StringUtils.sanitizePreservingNulls
import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull

final class CheckstyleParser {

    List<Violation> parseViolations(final Project project, final String reportText, final String reporterName) {
        GPathResult xml = new XmlSlurper().parseText(reportText)

        final List<Violation> result = new ArrayList<>()

        xml.file.each { file ->
            file.error.each { violation ->
                final String fullViolationName = sanitizeToNonNull((String) violation.@source.text())
                final String shortViolationName = sanitizeToNonNull(
                        (String) fullViolationName.substring(fullViolationName.lastIndexOf(".") + 1))

                final String lineNumberString = sanitizeToNonNull((String) violation.@line.text())
                final Integer lineNumber = LineNumberParser.parseLineNumberString(
                        lineNumberString,
                        reporterName,
                        shortViolationName,
                        project.getLogger())

                result.add(new Violation(
                        shortViolationName,
                        reporterName,
                        sanitizePreservingNulls((String) violation.@message.text()),
                        PathCalculator.calculatePathWithinProject(project, (String) file.@name.text()),
                        lineNumber))
            }
        }

        return result
    }

}
