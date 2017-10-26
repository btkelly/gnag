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
import com.btkelly.gnag.tasks.GnagCheck
import com.btkelly.gnag.tasks.GnagReportTask
import com.btkelly.gnag.utils.ProjectHelper
import groovy.lang.Closure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.JavaExec

import java.util.HashMap
import java.util.Map

/**
 * Created by bobbake4 on 4/1/16.
 */
class GnagPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        GnagPluginExtension gnagPluginExtension = GnagPluginExtension.loadExtension(project)

        project.afterEvaluate { evaluatedProject ->
            ProjectHelper projectHelper = new ProjectHelper(evaluatedProject)

            Map<String, Object> taskOptions = new HashMap<>()

            taskOptions.put(Task.TASK_NAME, "ktlint")
            taskOptions.put(Task.TASK_TYPE, Exec.class)
            taskOptions.put(Task.TASK_GROUP, "Verification")
            taskOptions.put(Task.TASK_DESCRIPTION, "Runs ktlint and generates an XML report")

            // ktlint fails unless the report file is created ahead of time; not sure why
            // https://github.com/shyiko/ktlint/blob/bcdb9c32ef615f7057af779fe5075d7fb24d74b9/ktlint/src/main/kotlin/com/github/shyiko/ktlint/Main.kt#L278
            evaluatedProject.task(taskOptions, "ktlint") { task ->
                // does not work to pre-create the file...
                project.file(projectHelper.getKtlintReportFile())

                commandLine 'ktlint', "--debug", "--reporter=checkstyle,output=${projectHelper.getKtlintReportFile()}"
                ignoreExitValue = true
            }

            GnagCheck.addTask(projectHelper, gnagPluginExtension)
            GnagReportTask.addTask(projectHelper, gnagPluginExtension.github)
        }
    }
}
