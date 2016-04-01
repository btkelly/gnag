/**
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
package com.btkelly.gnag.tasks;

import com.btkelly.gnag.GnagExtension;
import com.btkelly.gnag.reporters.CheckstyleReporter;
import com.btkelly.gnag.reporters.FindbugsReporter;
import com.btkelly.gnag.reporters.PMDReporter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagCheck extends DefaultTask {

    public static final String TASK_NAME = "gnagCheck";

    public static void addTask(Project project) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(Task.TASK_TYPE, GnagCheck.class);
        taskOptions.put(Task.TASK_GROUP, "Verification");
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag checks and generates an HTML report");

        Task gnagCheckTask = project.task(taskOptions, TASK_NAME);

        GnagExtension gnagExtension = project.getExtensions().create(GnagExtension.NAME, GnagExtension.class, project);

        gnagCheckTask.dependsOn(
                new CheckstyleReporter(gnagExtension.checkStyle, project),
                new PMDReporter(gnagExtension.pmd, project),
                new FindbugsReporter(gnagExtension.findbugs, project)
        );
    }
}
