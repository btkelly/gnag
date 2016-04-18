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
import com.btkelly.gnag.utils.GnagReportBuilder;
import org.gradle.api.*;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
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
        gnagCheckTask.setGnagPluginExtension(gnagPluginExtension);
        gnagCheckTask.reporters.add(new CheckstyleReporter(gnagPluginExtension.checkstyle, project));
        gnagCheckTask.reporters.add(new PMDReporter(gnagPluginExtension.pmd, project));
        gnagCheckTask.reporters.add(new FindbugsReporter(gnagPluginExtension.findbugs, project));
    }

    private final List<BaseReporter> reporters = new ArrayList<>();
    private GnagPluginExtension gnagPluginExtension;

    @TaskAction
    public void taskAction() {
        if (gnagPluginExtension.isEnabled()) {
            executeGnagCheck();
        }
    }

    private void executeGnagCheck() {
        boolean foundErrors = false;

        File reportsDir = new File(getProject().getBuildDir().getPath() + "/outputs/gnag/");
        GnagReportBuilder gnagReportBuilder = new GnagReportBuilder(reportsDir);

        for (BaseReporter baseReporter : reporters) {

            if (baseReporter.isEnabled()) {
                baseReporter.executeReporter();

                if (baseReporter.hasErrors()) {
                    foundErrors = true;
                    baseReporter.appendReport(gnagReportBuilder);
                }
            }
        }

        gnagReportBuilder.writeFile();

        if (foundErrors && gnagPluginExtension.shouldFailOnError()) {
            throw new GradleException("One or more reporters has caused the build to fail");
        }
    }

    private void setGnagPluginExtension(GnagPluginExtension gnagPluginExtension) {
        this.gnagPluginExtension = gnagPluginExtension;
    }
}
