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

import com.btkelly.gnag.GnagPluginExtension;
import com.btkelly.gnag.reporters.BaseReporter;
import com.btkelly.gnag.reporters.CheckstyleReporter;
import com.btkelly.gnag.reporters.FindbugsReporter;
import com.btkelly.gnag.reporters.PMDReporter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagCheck extends DefaultTask {

    public static final String TASK_NAME = "gnagCheck";

    public static void addTask(Project project, GnagPluginExtension gnagPluginExtension) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(Task.TASK_TYPE, GnagCheck.class);
        taskOptions.put(Task.TASK_GROUP, "Verification");
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag checks and generates an HTML report");

        GnagCheck gnagCheckTask = (GnagCheck) project.task(taskOptions, TASK_NAME);

        if (gnagPluginExtension.checkstyle.isEnabled()) {
            gnagCheckTask.reporters.add(new CheckstyleReporter(gnagPluginExtension.checkstyle, project));
        }

        if (gnagPluginExtension.pmd.isEnabled()) {
            gnagCheckTask.reporters.add(new PMDReporter(gnagPluginExtension.pmd, project));
        }

        if (gnagPluginExtension.findbugs.isEnabled()) {
            gnagCheckTask.reporters.add(new FindbugsReporter(gnagPluginExtension.findbugs, project));
        }
    }

    private final List<BaseReporter> reporters = new ArrayList<>();

    @TaskAction
    public void taskAction() {
        reporters.forEach(BaseReporter::executeReporter);
    }
}
