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

import static com.btkelly.gnag.GnagPlugin.STD_ERR_LOG_LEVEL
import static com.btkelly.gnag.GnagPlugin.STD_OUT_LOG_LEVEL

/**
 * Created by bobbake4 on 4/1/16.
 */
class DetektViolationDetector extends BaseViolationDetector {

    private final ProjectHelper projectHelper = new ProjectHelper(project)
    private final CheckstyleParser checkstyleParser = new CheckstyleParser()

    DetektViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
    }

    @Override
    List<Violation> getDetectedViolations() {
        return checkstyleParser.parseViolations(project, reportFile().text, name())
    }

    @Override
    String name() {
        return "detekt"
    }

    @Override
    File reportFile() {
        return new File(projectHelper.getReportsDir(), "${projectHelper.getDetektReportFileName()}")
    }

    static ViolationDetector configure(ProjectHelper projectHelper, GnagCheckTask gnagCheckTask, GnagPluginExtension gnagPluginExtension) {
        if (gnagPluginExtension.detekt.isEnabled() && projectHelper.hasKotlinSourceFiles()) {
            String overrideToolVersion = gnagPluginExtension.detekt.getToolVersion()
            String toolVersion = overrideToolVersion != null ? overrideToolVersion : GnagCheckTask.DETEKT_TOOL_VERSION

            projectHelper.project.getConfigurations().create("gnagDetekt")
            projectHelper.project.getDependencies().add("gnagDetekt", "io.gitlab.arturbosch.detekt:detekt-cli:" + toolVersion)

            Task detektTask = addTask(projectHelper, gnagPluginExtension.detekt.getReporterConfig())
            gnagCheckTask.dependsOn(detektTask)
            return new DetektViolationDetector(projectHelper.project, gnagPluginExtension.detekt)
        }

        return null
    }

    static Task addTask(ProjectHelper projectHelper, final File reporterConfig) {
        Map<String, Object> taskOptions = new HashMap<>()

        taskOptions.put(Task.TASK_NAME, "gnagDetekt")
        taskOptions.put(Task.TASK_TYPE, JavaExec.class)
        taskOptions.put(Task.TASK_GROUP, "Verification")
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs detekt and generates an XML report for parsing by Gnag")

        final Task result = projectHelper.project.task(taskOptions, "gnagDetekt") { task ->
            main = "io.gitlab.arturbosch.detekt.cli.Main"
            classpath = projectHelper.project.configurations.gnagDetekt
            ignoreExitValue = true
            args "--report", "xml:${projectHelper.getReportsDir()}/${projectHelper.getDetektReportFileName()}"
            args "--input", "${projectHelper.kotlinSourceFiles.join(',')}"

            if (reporterConfig != null) {
                args "--config", "$reporterConfig"
            }
        }

        result.logging.captureStandardOutput(STD_OUT_LOG_LEVEL)
        result.logging.captureStandardError(STD_ERR_LOG_LEVEL)

        return result
    }

}
