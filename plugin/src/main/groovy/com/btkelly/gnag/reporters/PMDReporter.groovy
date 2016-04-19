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
import com.btkelly.gnag.utils.GnagReportBuilder
import groovy.util.slurpersupport.GPathResult
import net.sourceforge.pmd.ant.PMDTask
import org.gradle.api.Project

/**
 * Created by bobbake4 on 4/1/16.
 */
class PMDReporter extends BaseExecutedReporter {

    PMDReporter(ReporterExtension reporterExtension, Project project) {
        super(reporterExtension, project)
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
            pmdTask.ruleSetFiles = getClass().getClassLoader().getResource("pmd.xml").toString()
        }

        reportHelper.getAndroidSources().findAll { it.exists() }.each {
            pmdTask.addFileset(project.ant.fileset(dir: it))
        }

        pmdTask.perform()
    }

    @Override
    boolean hasErrors() {
        GPathResult xml = new XmlSlurper().parseText(reportFile().text)
        int numErrors = xml.file.inject(0) { count, file -> count + file.violation.size() }
        println "PMD report executed, found " + numErrors + " errors."
        return numErrors != 0
    }

    @Override
    String reporterName() {
        return "PMD"
    }

    @Override
    File reportFile() {
        return new File(reportHelper.getReportsDir(), "pmd.xml")
    }

    @Override
    void appendReport(GnagReportBuilder gnagReportBuilder) {

        gnagReportBuilder.insertReporterHeader(reporterName())

        GPathResult xml = new XmlSlurper().parseText(reportFile().text)

        xml.file.each { file ->
            file.violation.each { violation ->
                gnagReportBuilder.appendViolation(
                        violation.@rule.text(),
                        violation.@externalInfoUrl.text(),
                        file.@name.text(),
                        violation.@beginline.text(),
                        violation.text()
                )
            }
        }
    }
}
