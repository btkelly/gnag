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
package com.btkelly.gnag

import com.btkelly.gnag.extensions.GnagPluginExtension
import com.btkelly.gnag.tasks.BaseGnagCheckTask
import com.btkelly.gnag.tasks.GnagDetektTask
import com.btkelly.gnag.tasks.GnagAndroidLintTask
import com.btkelly.gnag.tasks.GnagCheckStyleTask
import com.btkelly.gnag.tasks.GnagCheckTask

import com.btkelly.gnag.tasks.GnagFindBugsTask
import com.btkelly.gnag.tasks.GnagKtlintTask
import com.btkelly.gnag.tasks.GnagPmdTask
import com.btkelly.gnag.tasks.GnagReportTask
import com.btkelly.gnag.utils.ProjectHelper
import org.gradle.api.AntBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

/**
 * Created by bobbake4 on 4/1/16.
 */
class GnagPlugin implements Plugin<Project> {

    public static final LogLevel STD_OUT_LOG_LEVEL = LogLevel.DEBUG
    public static final LogLevel STD_ERR_LOG_LEVEL = LogLevel.ERROR

    @Override
    void apply(Project project) {
        project.logging.captureStandardOutput(STD_OUT_LOG_LEVEL)
        project.logging.captureStandardError(STD_ERR_LOG_LEVEL)
        project.ant.setLifecycleLogLevel(AntBuilder.AntMessagePriority.ERROR)

        GnagPluginExtension gnagPluginExtension = GnagPluginExtension.loadExtension(project)

        project.repositories.jcenter()
        // Unlikely to be missing in real projects; here for sample projects only.

        project.configurations.create("gnagDetektExternal")
        project.dependencies.add("gnagDetektExternal", "io.gitlab.arturbosch.detekt:detekt-cli:1.0.0.RC7-3")

        String overrideToolVersion = gnagPluginExtension.ktlint.getToolVersion()
        String toolVersion = overrideToolVersion != null ? overrideToolVersion : "0.24.0"
        project.configurations.create("gnagKtlintExternal")
        project.dependencies.add("gnagKtlintExternal", "com.github.shyiko:ktlint:" + toolVersion)

        project.afterEvaluate { evaluatedProject ->
            ProjectHelper projectHelper = new ProjectHelper(evaluatedProject)

            List<BaseGnagCheckTask> tasks = new ArrayList<>()

            tasks.add(GnagCheckStyleTask.addTask(projectHelper, gnagPluginExtension))
            tasks.add(GnagPmdTask.addTask(projectHelper, gnagPluginExtension))
            tasks.add(GnagFindBugsTask.addTask(projectHelper, gnagPluginExtension))
            tasks.add(GnagKtlintTask.addTask(projectHelper, gnagPluginExtension))
            tasks.add(GnagDetektTask.addTask(projectHelper, gnagPluginExtension))
            tasks.add(GnagAndroidLintTask.addTask(projectHelper, gnagPluginExtension))

            GnagCheckTask.addTask(projectHelper, gnagPluginExtension, tasks)

            GnagReportTask.addTask(projectHelper, gnagPluginExtension)
        }
    }
}
