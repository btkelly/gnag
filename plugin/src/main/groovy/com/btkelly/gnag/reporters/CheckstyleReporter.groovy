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
import com.puppycrawl.tools.checkstyle.ant.CheckstyleAntTask
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project

/**
 * Created by bobbake4 on 4/1/16.
 */
class CheckstyleReporter extends BaseReporter {

    CheckstyleReporter(ReporterExtension reporterExtension, Project project) {
        super(reporterExtension, project)
    }

    @Override
    void executeReporter() {

        CheckstyleAntTask checkStyleTask = new CheckstyleAntTask()
        checkStyleTask.project = project.ant.antProject
        checkStyleTask.failOnViolation = false
        checkStyleTask.addFormatter(new CheckstyleAntTask.Formatter(type: new CheckstyleAntTask.FormatterType(value: 'xml'), tofile: reportFile()))

        if (reporterExtension.hasReporterConfig()) {
            checkStyleTask.setConfig(reporterExtension.getReporterConfig())
        } else {
            checkStyleTask.setConfigUrl(getClass().getClassLoader().getResource("checkstyle.xml"))
        }

        getAndroidSources().findAll { it.exists() }.each {
            checkStyleTask.addFileset(project.ant.fileset(dir: it))
        }

        checkStyleTask.perform()
    }

    @Override
    boolean hasErrors() {
        GPathResult xml = new XmlSlurper().parseText(reportFile().text)
        int numErrors = xml.file.inject(0) { count, file -> count + file.error.size() }
        println "Checkstyle report executed, found " + numErrors + " errors."
        return numErrors != 0
    }

    @Override
    String reporterName() {
        return "checkstyle"
    }

    @Override
    File reportFile() {
        return new File(getReportsDir(), "checkstyle_report.xml")
    }
}
