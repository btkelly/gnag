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
import com.btkelly.gnag.utils.StringUtils
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project

import static com.btkelly.gnag.extensions.AndroidLintExtension.SEVERITY_ERROR
import static com.btkelly.gnag.extensions.AndroidLintExtension.SEVERITY_WARNING
/**
 * Created by bobbake4 on 4/18/16.
 */
class AndroidLintViolationDetector implements ViolationDetector {

    private final Project project
    private final AndroidLintExtension androidLintExtension;

    AndroidLintViolationDetector(AndroidLintExtension androidLintExtension, Project project) {
        this.project = project
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
                        final Integer lineNumber;
            
                        try {
                            lineNumber = violation.location.@line.toInteger()
                        } catch (final NumberFormatException e) {
                            System.out.println("Error reading line number from Android Lint violations.");
                            e.printStackTrace();
                            lineNumber = null
                        }

                        result.add(new Violation(
                                StringUtils.sanitize((String) violation.@id.text()),
                                StringUtils.sanitize((String) name()),
                                StringUtils.sanitize((String) violation.@message.text()),
                                StringUtils.sanitize((String) violation.@url.text()),
                                StringUtils.sanitize((String) violation.location.@file.text()), // todo: make relative
                                lineNumber))
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

    private boolean severityEnabled(String severity) {
        if (androidLintExtension.severity.equals(SEVERITY_WARNING)) {
            return true
        } else {
            return severity.equals(SEVERITY_ERROR)
        }
    }
}
