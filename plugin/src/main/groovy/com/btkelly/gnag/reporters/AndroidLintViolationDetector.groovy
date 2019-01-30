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
import com.btkelly.gnag.reporters.utils.LineNumberParser
import com.btkelly.gnag.reporters.utils.PathCalculator
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter
import com.vladsch.flexmark.util.options.DataHolder
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.GradleException
import org.gradle.api.Project

import static com.btkelly.gnag.extensions.AndroidLintExtension.SEVERITY_ERROR
import static com.btkelly.gnag.extensions.AndroidLintExtension.SEVERITY_WARNING
import static com.btkelly.gnag.utils.StringUtils.sanitizePreservingNulls
import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull
import static com.vladsch.flexmark.profiles.pegdown.Extensions.*

class AndroidLintViolationDetector extends BaseViolationDetector {

    private static final int FLEXMARK_GFM_OPTIONS = HARDWRAPS | AUTOLINKS | FENCED_CODE_BLOCKS

    private final AndroidLintExtension androidLintExtension

    private final Parser parser
    private final HtmlRenderer renderer

    AndroidLintViolationDetector(final Project project, final isEnabled, final AndroidLintExtension androidLintExtension) {
        super(project, isEnabled)
        this.androidLintExtension = androidLintExtension

        final DataHolder options = PegdownOptionsAdapter.flexmarkOptions(FLEXMARK_GFM_OPTIONS)
        parser = Parser.builder(options).build()
        renderer = HtmlRenderer.builder().build()
    }

    @Override
    List<Violation> getDetectedViolations() {
        GPathResult xml = new XmlSlurper().parseText(reportFile().text)

        final List<Violation> result = new ArrayList<>()

        xml.issue.findAll { severityEnabled((String) it.@severity.text()) }
                .each { violation ->
            final String violationType = sanitizeToNonNull((String) violation.@id.text())

            final String commentInMarkdown = sanitizeToNonNull((String) violation.@message.text())

            final String notNullCommentInHtml = sanitizeToNonNull(renderer.render(parser.parse(commentInMarkdown)))
                    .replaceAll("</?p>", "")

            final String nullableCommentInHtml = notNullCommentInHtml.isEmpty() ? null : notNullCommentInHtml

            final String lineNumberString = sanitizeToNonNull((String) violation.location.@line.text())
            final Integer lineNumber = LineNumberParser.parseLineNumberString(
                    lineNumberString,
                    name(),
                    violationType,
                    project.getLogger())

            final List<String> secondaryUrls = computeSecondaryUrls(
                    sanitizePreservingNulls((String) violation.@urls.text()),
                    nullableCommentInHtml)

            result.add(new Violation(
                    violationType,
                    name(),
                    nullableCommentInHtml,
                    PathCalculator.calculatePathWithinProject(project, (String) violation.location.@file.text()),
                    lineNumber,
                    null,
                    secondaryUrls))
        }

        return result
    }

    @Override
    String name() {
        return "Android Lint"
    }

    @Override
    File reportFile() {
        List<String> reportFileNames = new FileNameByRegexFinder()
                .getFileNames(project.buildDir.path, 'lint-results.*\\.xml')

        if (reportFileNames.isEmpty()) {
            throw new GradleException("Lint XML report file not found; " +
                    "check that android.lintOptions.xmlReport is set to true in your build.gradle file.")
        } else {
            return new File(reportFileNames.first()) // todo: handle >1 result better
        }
    }

    private boolean severityEnabled(final String severity) {
        if (androidLintExtension.severity.equals(SEVERITY_WARNING)) {
            return true
        } else {
            return severity.equals(SEVERITY_ERROR)
        }
    }

    static List<String> computeSecondaryUrls(final String secondaryUrlsString, final String nullableCommentInHtml) {
        if (secondaryUrlsString == null) {
            return new ArrayList<>()
        }

        // Uses positive lookaround to avoiding splitting at comma characters _within_ URLs.
        final List<String> parsedUrls = Arrays.asList(secondaryUrlsString.split(",(?=\\s*https?)"))

        if (nullableCommentInHtml == null) {
            return parsedUrls
        }

        final List<String> result = new ArrayList<>()

        // Remove any URLs already present in the violation comment; no need to duplicate these.
        for (final String parsedUrl : parsedUrls) {
            if (!nullableCommentInHtml.contains(parsedUrl)) {
                result.add(parsedUrl)
            }
        }

        return result
    }

}
