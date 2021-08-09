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
import com.btkelly.gnag.reporters.utils.CheckstyleParser
import com.btkelly.gnag.tasks.GnagCheckTask
import com.btkelly.gnag.utils.ProjectHelper
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.JavaExec

/**
 * Created by bobbake4 on 4/1/16.
 */
class KtlintViolationDetector extends BaseViolationDetector {

    private final ProjectHelper projectHelper = new ProjectHelper(project)
    private final CheckstyleParser checkstyleParser = new CheckstyleParser()

    KtlintViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
    }

    @Override
    List<Violation> getDetectedViolations() {
        return checkstyleParser.parseViolations(project, reportFile().text, name())
    }

    @Override
    String name() {
        return "ktlint"
    }

    @Override
    File reportFile() {
        return projectHelper.getKtlintReportFile()
    }

    static ViolationDetector configure(ProjectHelper projectHelper, GnagCheckTask gnagCheckTask, GnagPluginExtension gnagPluginExtension) {
        if (gnagPluginExtension.ktlint.isEnabled() && projectHelper.hasKotlinSourceFiles()) {
            String overrideToolVersion = gnagPluginExtension.ktlint.getToolVersion()
            String toolVersion = overrideToolVersion != null ? overrideToolVersion : GnagCheckTask.KTLINT_TOOL_VERSION

            projectHelper.project.getConfigurations().create("gnagKtlint")
            projectHelper.project.getDependencies().add("gnagKtlint", "com.pinterest:ktlint:" + toolVersion)

            Task ktlintTask = addTask(projectHelper)
            gnagCheckTask.dependsOn(ktlintTask)
            return new KtlintViolationDetector(projectHelper.project, gnagPluginExtension.ktlint)
        }

        return null
    }

    private static Task addTask(ProjectHelper projectHelper) {
        Map<String, Object> taskOptions = new HashMap<>()

        taskOptions.put(Task.TASK_NAME, "gnagKtlint")
        taskOptions.put(Task.TASK_TYPE, JavaExec.class)
        taskOptions.put(Task.TASK_GROUP, "Verification")
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs ktlint and generates an XML report for parsing by Gnag")

        return projectHelper.project.task(taskOptions, "gnagKtlint") { task ->
            main = "com.pinterest.ktlint.Main"
            classpath = projectHelper.project.configurations.gnagKtlint
            ignoreExitValue = true
            args "--reporter=checkstyle,output=${projectHelper.getKtlintReportFile()}"

            projectHelper.kotlinSourceFiles.forEach { sourceFile ->
                args "${sourceFile.absoluteFile}"
            }
        }
    }

}
