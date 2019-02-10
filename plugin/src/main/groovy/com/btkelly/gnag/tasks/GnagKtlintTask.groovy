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
package com.btkelly.gnag.tasks

import com.btkelly.gnag.extensions.GnagPluginExtension
import com.btkelly.gnag.reporters.ViolationDetectorFactory
import com.btkelly.gnag.reporters.ViolationResolver
import com.btkelly.gnag.utils.ProjectHelper
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.JavaExec

import java.util.logging.Logger

class GnagKtlintTask extends BaseGnagCheckTask implements ViolationResolver {
    private static final String TASK_NAME = "gnagKtlint"
    private static final String TASK_NAME_EXTERNAL = "gnagKtlintExternal"

    static BaseGnagCheckTask addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension) {
        Map<String, Object> taskOptionsInternal = new HashMap<>()
        taskOptionsInternal.put(Task.TASK_NAME, TASK_NAME)
        taskOptionsInternal.put(Task.TASK_TYPE, GnagKtlintTask.class)
        taskOptionsInternal.put(Task.TASK_GROUP, "Verification")
        taskOptionsInternal.put(Task.TASK_DESCRIPTION, "Runs ktlint and generates an XML report")

        Map<String, Object> taskOptionsExternal = new HashMap<>()
        taskOptionsExternal.put(Task.TASK_NAME, TASK_NAME_EXTERNAL)
        taskOptionsExternal.put(Task.TASK_TYPE, JavaExec.class)
        taskOptionsExternal.put(Task.TASK_GROUP, "Verification")
        taskOptionsExternal.put(Task.TASK_DEPENDS_ON, "check")
        taskOptionsExternal.put(Task.TASK_DESCRIPTION, "Runs ktlint and generates an XML report")

        final result = (DefaultTask) projectHelper.project.task(taskOptionsExternal, TASK_NAME_EXTERNAL) { task ->
            main = "com.github.shyiko.ktlint.Main"
            classpath = projectHelper.project.configurations.gnagKtlintExternal
            ignoreExitValue = true
            args "--reporter=checkstyle,output=${projectHelper.getKtlintReportFile()}"

            projectHelper.kotlinSourceFiles.forEach { sourceFile ->
                args "${sourceFile.absoluteFile}"
            }
        }

        Project project = projectHelper.getProject()
        GnagKtlintTask gnagKtlintTask = (GnagKtlintTask) project.task(taskOptionsInternal, TASK_NAME)
        gnagKtlintTask.dependsOn(result)

        gnagKtlintTask.gnagPluginExtension = gnagPluginExtension
        gnagKtlintTask.resolve(projectHelper.project)

        return gnagKtlintTask
    }

    @Override
    void resolve(Project project) {
        violationDetectors.add(ViolationDetectorFactory.getKtLintViolationDetector(project, super.gnagPluginExtension))
    }
}
