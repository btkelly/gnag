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

import static com.btkelly.gnag.GnagPlugin.STD_ERR_LOG_LEVEL
import static com.btkelly.gnag.GnagPlugin.STD_OUT_LOG_LEVEL

class GnagDetektTask extends BaseGnagCheckTask implements ViolationResolver {
    private static final String TASK_NAME = "gnagDetekt"
    private static final String TASK_NAME_EXTERNAL = "gnagDetektExternal"

    static BaseGnagCheckTask addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension) {
        Map<String, Object> taskOptionsInternal = new HashMap<>()
        taskOptionsInternal.put(Task.TASK_NAME, TASK_NAME)
        taskOptionsInternal.put(Task.TASK_TYPE, GnagDetektTask.class)
        taskOptionsInternal.put(Task.TASK_GROUP, "Verification")
        taskOptionsInternal.put(Task.TASK_DESCRIPTION, "Runs detekt and generates an XML report")

        Map<String, Object> taskOptionsExternal = new HashMap<>()
        taskOptionsExternal.put(Task.TASK_NAME, TASK_NAME_EXTERNAL)
        taskOptionsExternal.put(Task.TASK_TYPE, JavaExec.class)
        taskOptionsExternal.put(Task.TASK_GROUP, "Verification")
        taskOptionsExternal.put(Task.TASK_DEPENDS_ON, "check")
        taskOptionsExternal.put(Task.TASK_DESCRIPTION, "Runs detekt and generates an XML report")

        final result = (DefaultTask) projectHelper.project.task(taskOptionsExternal, TASK_NAME_EXTERNAL) { task ->
            main = "io.gitlab.arturbosch.detekt.cli.Main"
            classpath = projectHelper.project.configurations.gnagDetektExternal
            ignoreExitValue = true
            args "--input", "${projectHelper.kotlinSourceFiles.join(',')}"
            args "--output", "${projectHelper.getReportsDir()}"
            args "--output-name", "${projectHelper.getDetektReportFileName()}"

            if (gnagPluginExtension.detekt.reporterConfig != null) {
                args "--config", "$gnagPluginExtension.detekt.reporterConfig"
            }
        }

        result.logging.captureStandardOutput(STD_OUT_LOG_LEVEL)
        result.logging.captureStandardError(STD_ERR_LOG_LEVEL)

        Project project = projectHelper.getProject()
        GnagDetektTask gnagDetektTask = (GnagDetektTask) project.task(taskOptionsInternal, TASK_NAME)
        gnagDetektTask.dependsOn(result)

        gnagDetektTask.setGnagPluginExtension(gnagPluginExtension)
        gnagDetektTask.resolve(projectHelper.project)

        return gnagDetektTask
    }

    @Override
    void resolve(Project project) {
        violationDetectors.add(ViolationDetectorFactory.getDetektViolationDetector(project, super.gnagPluginExtension))
    }
}
