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

import com.btkelly.gnag.extensions.ReporterExtension
import com.btkelly.gnag.models.Violation
import com.btkelly.gnag.reporters.utils.LineNumberParser
import com.btkelly.gnag.reporters.utils.PathCalculator
import groovy.util.slurpersupport.GPathResult
import net.sourceforge.pmd.ant.PMDTask
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import static com.btkelly.gnag.utils.StringUtils.sanitizePreservingNulls
import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull
/**
 * Created by bobbake4 on 4/1/16.
 */
class PMDViolationDetector extends BaseExecutedViolationDetector {

    PMDViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
    }

    @Override
    void executeReporter() {

        PMDTask pmdTask = new PMDTask()
        pmdTask.project = project.ant.antProject
        pmdTask.addFormatter(new net.sourceforge.pmd.ant.Formatter(type: 'xml', toFile: reportFile()))

        pmdTask.failOnError = false
        pmdTask.failOnRuleViolation = false

        if (reporterExtension.hasReporterConfig()) {
            pmdTask.ruleSetFiles = reporterExtension.getReporterConfig().toString()
        } else {
            final InputStream defaultPmdRulesInputStream = getClass().getClassLoader().getResourceAsStream("pmd.xml")
            final File tempPmdRuleSetFile = File.createTempFile("pmdRuleSetFile", null)
            tempPmdRuleSetFile.deleteOnExit()
            FileUtils.copyInputStreamToFile(defaultPmdRulesInputStream, tempPmdRuleSetFile)
            pmdTask.ruleSetFiles = tempPmdRuleSetFile
        }

        projectHelper.getJavaSources().findAll { it.exists() }.each {
            pmdTask.addFileset(project.ant.fileset(dir: it))
        }

        pmdTask.perform()
    }

    @Override
    List<Violation> getDetectedViolations() {
        GPathResult xml = new XmlSlurper().parseText(reportFile().text)

        final List<Violation> result = new ArrayList<>()

        xml.file.each { file ->
            file.violation.each { violation ->
                final String violationType = sanitizeToNonNull((String) violation.@rule.text())

                final String lineNumberString = sanitizeToNonNull((String) violation.@endline.text())
                final Integer lineNumber = LineNumberParser.parseLineNumberString(
                        lineNumberString,
                        name(),
                        violationType)

                result.add(new Violation(
                        violationType,
                        name(),
                        sanitizePreservingNulls((String) violation.text()),
                        PathCalculator.calculatePathWithinProject(project, (String) file.@name.text()),
                        lineNumber,
                        sanitizePreservingNulls((String) violation.@externalInfoUrl.text())))
            }
        }

        return result
    }

    @Override
    String name() {
        return "PMD"
    }

    @Override
    File reportFile() {
        return new File(projectHelper.getReportsDir(), "pmd.xml")
    }

}
