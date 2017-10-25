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
import com.btkelly.gnag.reporters.utils.CheckstyleParser
import com.puppycrawl.tools.checkstyle.ant.CheckstyleAntTask
import org.gradle.api.Project

/**
 * Created by bobbake4 on 4/1/16.
 */
class CheckstyleViolationDetector extends BaseExecutedViolationDetector {

    private final CheckstyleParser checkstyleParser = new CheckstyleParser()

    CheckstyleViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
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

        projectHelper.getJavaSources().findAll { it.exists() }.each {
            checkStyleTask.addFileset(project.ant.fileset(dir: it))
        }

        checkStyleTask.perform()
    }

    @Override
    List<Violation> getDetectedViolations() {
        return checkstyleParser.parseViolations(project, reportFile().text, name())
    }

    @Override
    String name() {
        return "Checkstyle"
    }

    @Override
    File reportFile() {
        return new File(projectHelper.getReportsDir(), "checkstyle_report.xml")
    }

}
