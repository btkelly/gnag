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
package com.btkelly.gnag.reporters

import com.btkelly.gnag.extensions.AndroidLintExtension
import com.btkelly.gnag.models.Violation
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.pegdown.PegDownProcessor

import static com.btkelly.gnag.extensions.AndroidLintExtension.SEVERITY_ERROR
import static com.btkelly.gnag.extensions.AndroidLintExtension.SEVERITY_WARNING
import static com.btkelly.gnag.utils.StringUtils.sanitizePreservingNulls
import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull
import static org.pegdown.Extensions.*
/**
 * Created by bobbake4 on 4/18/16.
 */
class AndroidLintViolationDetector extends BaseViolationDetector {
    
    private static final int GFM_PEGDOWN_PROCESSOR_EXTENSIONS = HARDWRAPS | AUTOLINKS | FENCED_CODE_BLOCKS

    private final AndroidLintExtension androidLintExtension;

    AndroidLintViolationDetector(final Project project, final AndroidLintExtension androidLintExtension) {
        super(project)
        this.androidLintExtension = androidLintExtension
    }

    @Override
    boolean isEnabled() {

        if (androidLintExtension.enabled) {
            if (reportFile().exists()) {
                return true
            } else {
                println "Android Lint ViolationDetector is enabled but no lint violations was found"
                return false
            }
        } else {
            return false
        }
    }

    @Override
    List<Violation> getDetectedViolations() {
        GPathResult xml = new XmlSlurper().parseText(reportFile().text)

        final List<Violation> result = new ArrayList<>()

        xml.issue.findAll { severityEnabled((String) it.@severity.text()) }
            .each { violation ->
                final String violationType = sanitizeToNonNull((String) violation.@id.text())
            
                final String messageInMarkdown = sanitizeToNonNull((String) violation.@message.text())
                final PegDownProcessor markdownProcessor = new PegDownProcessor(GFM_PEGDOWN_PROCESSOR_EXTENSIONS)
                final String notNullMessageInHtml =
                        sanitizeToNonNull(markdownProcessor.markdownToHtml(messageInMarkdown))
                        .replaceAll("</?p>", "")
            
                final String nullableMessageInHtml = notNullMessageInHtml.isEmpty() ? null : notNullMessageInHtml
            
                final String lineNumberString = sanitizeToNonNull((String) violation.location.@line.text())
                final Integer lineNumber = computeLineNumberFromString(lineNumberString, violationType)
            
                result.add(new Violation(
                        violationType,
                        name(),
                        nullableMessageInHtml,
                        computeFilePathRelativeToProjectRoot((String) violation.location.@file.text()),
                        lineNumber,
                        sanitizePreservingNulls((String) violation.@url.text())))
            }

        return result
    }

    @Override
    String name() {
        return "Android Lint"
    }

    @Override
    File reportFile() {
        return new File(new FileNameByRegexFinder().getFileNames(project.buildDir.path + "/outputs/", "lint-results.+\\.xml").first())
    }

    private boolean severityEnabled(final String severity) {
        if (androidLintExtension.severity.equals(SEVERITY_WARNING)) {
            return true
        } else {
            return severity.equals(SEVERITY_ERROR)
        }
    }
}
