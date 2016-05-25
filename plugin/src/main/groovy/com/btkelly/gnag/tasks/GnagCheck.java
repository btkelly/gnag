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

import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.models.CheckStatus;
import com.btkelly.gnag.reporters.*;
import com.btkelly.gnag.utils.GnagReportBuilder;
import com.btkelly.gnag.utils.ReportHelper;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionGraph;
import org.gradle.api.tasks.StopExecutionException;
import org.gradle.api.tasks.TaskAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.btkelly.gnag.models.GitHubStatusType.FAILURE;
import static com.btkelly.gnag.models.GitHubStatusType.SUCCESS;

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
        gnagCheckTask.reporters.add(new AndroidLintReporter(gnagPluginExtension.androidLint, project));
    }

    private final List<Reporter> reporters = new ArrayList<>();
    private GnagPluginExtension gnagPluginExtension;

    @TaskAction
    public void taskAction() {
        if (gnagPluginExtension.isEnabled()) {
            executeGnagCheck();
        }
    }

    private void executeGnagCheck() {
        boolean foundErrors = false;

        ReportHelper reportHelper = new ReportHelper(getProject());
        GnagReportBuilder gnagReportBuilder = new GnagReportBuilder(getProject());

        for (Reporter reporter : reporters) {

            if (reporter.isEnabled()) {

                if (reporter instanceof BaseExecutedReporter) {
                    ((BaseExecutedReporter) reporter).executeReporter();
                }

                if (reporter.foundViolations()) {
                    foundErrors = true;
                    reporter.appendReport(gnagReportBuilder);
                }
            }
        }

        gnagReportBuilder.writeReportToDirectory(reportHelper.getReportsDir());

        if (foundErrors) {

            getProject().setStatus(new CheckStatus(gnagReportBuilder.toString(), FAILURE));

            TaskExecutionGraph taskGraph = getProject().getGradle().getTaskGraph();

            boolean hasReportTask = false;

            for (Task task : taskGraph.getAllTasks()) {
                if (task.getName().equals(GnagReportTask.TASK_NAME)) {
                    hasReportTask = true;
                }
            }

            String failedMessage = "One or more reporters has found violations";

            if (gnagPluginExtension.shouldFailOnError() && !hasReportTask) {
                throw new GradleException(failedMessage);
            } else {
                System.out.println(failedMessage);
                throw new StopExecutionException(failedMessage);
            }
        } else {
            getProject().setStatus(new CheckStatus("Congrats! No :poop: code found, this PR is safe to merge.", SUCCESS));
        }
    }

    private void setGnagPluginExtension(GnagPluginExtension gnagPluginExtension) {
        this.gnagPluginExtension = gnagPluginExtension;
    }
}
