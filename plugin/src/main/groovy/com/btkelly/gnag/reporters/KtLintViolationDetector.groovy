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
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec

import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * Created by bobbake4 on 4/1/16.
 */
class KtLintViolationDetector extends BaseExecutedViolationDetector {

    private final CheckstyleParser checkstyleParser = new CheckstyleParser()

    KtLintViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
    }

    @Override
    void executeReporter() {
        JavaExec javaExecTask = new JavaExec()
//        javaExecTask.setClasspath() // todo: this probably needs to happen?
        javaExecTask.setMain("com.github.shyiko.ktlint.Main")
        javaExecTask.setArgs(
                projectHelper.getSources().stream().filter(new Predicate<File>() {
                    @Override
                    boolean test(final File file) {
                        return file.name.endsWith(".kt")
                    }
                }).collect(Collectors.toList()))

        javaExecTask.args("--reporter=checkstyle,output=${reportFile().name}")

        javaExecTask.exec()
    }

    @Override
    List<Violation> getDetectedViolations() {
        return checkstyleParser.parseViolations(project, reportFile().text, name())
    }

    @Override
    String name() {
        return "KtLint"
    }

    @Override
    File reportFile() {
        return new File(projectHelper.getReportsDir(), "ktlint_report.xml")
    }

}
