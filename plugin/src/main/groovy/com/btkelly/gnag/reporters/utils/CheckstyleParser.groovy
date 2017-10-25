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
                        shortViolationName)

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
