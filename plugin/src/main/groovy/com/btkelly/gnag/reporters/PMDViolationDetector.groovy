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

import com.btkelly.gnag.extensions.GnagPluginExtension
import com.btkelly.gnag.extensions.ReporterExtension
import com.btkelly.gnag.models.Violation
import com.btkelly.gnag.reporters.utils.LineNumberParser
import com.btkelly.gnag.reporters.utils.PathCalculator
import com.btkelly.gnag.tasks.GnagCheckTask
import com.btkelly.gnag.utils.ProjectHelper
import groovy.xml.slurpersupport.GPathResult
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import static com.btkelly.gnag.utils.StringUtils.sanitizePreservingNulls
import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull

/**
 * Created by bobbake4 on 4/1/16.
 */
class PMDViolationDetector extends BaseViolationDetector {

    PMDViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
    }

    @Override
    List<Violation> getDetectedViolations() {
        GPathResult xml = new groovy.xml.XmlSlurper().parseText(reportFile().text)

        final List<Violation> result = new ArrayList<>()

        xml.file.each { file ->
            file.violation.each { violation ->
                final String violationType = sanitizeToNonNull((String) violation.@rule.text())

                final String lineNumberString = sanitizeToNonNull((String) violation.@endline.text())
                final Integer lineNumber = LineNumberParser.parseLineNumberString(
                        lineNumberString,
                        name(),
                        violationType,
                        project.getLogger())

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
        File parentDir = getPMDReportDir(projectHelper)
        return new File(parentDir, "main.xml")
    }

    static ViolationDetector configure(ProjectHelper projectHelper, GnagCheckTask gnagCheckTask, GnagPluginExtension gnagPluginExtension) {
        if (gnagPluginExtension.pmd.isEnabled() && projectHelper.hasJavaSourceFiles()) {
            String overrideToolVersion = gnagPluginExtension.pmd.getToolVersion()
            String toolVersion = overrideToolVersion != null ? overrideToolVersion : GnagCheckTask.PMD_TOOL_VERSION

            projectHelper.project.getConfigurations().create("gnagPMD")
            projectHelper.project.plugins.apply("pmd")
            projectHelper.project.pmd.toolVersion = toolVersion
            projectHelper.project.pmd.ignoreFailures = true
            projectHelper.project.pmd.reportsDir = getPMDReportDir(projectHelper)

            ReporterExtension reporterExtension = gnagPluginExtension.pmd

            if (reporterExtension.hasReporterConfig()) {
                projectHelper.project.pmd.ruleSetFiles = reporterExtension.getReporterConfig().toString()
            } else {
                final InputStream defaultPmdRulesInputStream = ViolationDetector.getClassLoader().getResourceAsStream("pmd.xml")
                final File tempPmdRuleSetFile = File.createTempFile("pmdRuleSetFile", null)
                tempPmdRuleSetFile.deleteOnExit()
                FileUtils.copyInputStreamToFile(defaultPmdRulesInputStream, tempPmdRuleSetFile)
                projectHelper.project.pmd.ruleSetFiles = projectHelper.project.files(tempPmdRuleSetFile)
            }

            gnagCheckTask.dependsOn("pmdMain")

            return new PMDViolationDetector(projectHelper.project, gnagPluginExtension.pmd)
        }

        return null
    }

    private static File getPMDReportDir(ProjectHelper projectHelper) {
        File parentDir = new File(projectHelper.getReportsDir(), "/pmd/")
        parentDir.mkdirs()
        return parentDir
    }

}
