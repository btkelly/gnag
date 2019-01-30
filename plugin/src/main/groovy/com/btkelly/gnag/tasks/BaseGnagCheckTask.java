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

import com.btkelly.gnag.GnagPlugin;
import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.models.CheckStatus;
import com.btkelly.gnag.models.Violation;
import com.btkelly.gnag.reporters.BaseExecutedViolationDetector;
import com.btkelly.gnag.reporters.ViolationDetector;
import com.btkelly.gnag.utils.ProjectHelper;
import com.btkelly.gnag.utils.ReportWriter;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.StopExecutionException;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.btkelly.gnag.models.GitHubStatusType.FAILURE;
import static com.btkelly.gnag.utils.ReportWriter.REPORT_FILE_NAME;

public abstract class BaseGnagCheckTask extends DefaultTask {
    protected final ProjectHelper projectHelper = new ProjectHelper(getProject());
    protected final List<ViolationDetector> violationDetectors = new ArrayList<>();
    protected GnagPluginExtension gnagPluginExtension;

    public void setGnagPluginExtension(GnagPluginExtension gnagPluginExtension) {
        this.gnagPluginExtension = gnagPluginExtension;
    }

    @SuppressWarnings("unused")
    @TaskAction
    public void taskAction() {
        if (gnagPluginExtension.isEnabled()) {
            executeGnagCheck();
        }
    }

    @NotNull
    public Logger getLogger() {
        return Logging.getLogger(GnagPlugin.class);
    }

    private void executeGnagCheck() {
        final Set<Violation> allDetectedViolations = new HashSet<>();

        violationDetectors.forEach(violationDetector -> {
            if (violationDetector instanceof BaseExecutedViolationDetector) {
                ((BaseExecutedViolationDetector) violationDetector).executeReporter();
            }

            final List<Violation> detectedViolations = violationDetector.getDetectedViolations();
            allDetectedViolations.addAll(detectedViolations);

            getLogger().lifecycle(
                    violationDetector.name() + " detected " + detectedViolations.size() + " violations.");
        });

        final File reportsDir = projectHelper.getReportsDir();

        if (allDetectedViolations.isEmpty()) {
            ReportWriter.deleteLocalReportFiles(reportsDir, getLogger());

            getProject().setStatus(CheckStatus.getSuccessfulCheckStatus());

            getLogger().lifecycle("Congrats, no poop code found!");
        } else {
            ReportWriter.writeLocalReportFiles(allDetectedViolations, reportsDir, getLogger());

            getProject().setStatus(new CheckStatus(FAILURE, allDetectedViolations));

            final String failedMessage
                    = "One or more violation detectors has found violations. Check the report at "
                    + reportsDir
                    + File.separatorChar
                    + REPORT_FILE_NAME + " for details.";

            if (gnagPluginExtension.shouldFailOnError() && !taskExecutionGraphIncludesGnagReport()) {
                throw new GradleException(failedMessage);
            } else {
                getLogger().lifecycle(failedMessage);
                throw new StopExecutionException(failedMessage);
            }
        }
    }

    private boolean taskExecutionGraphIncludesGnagReport() {
        for (final Task task : getProject().getGradle().getTaskGraph().getAllTasks()) {
            if (task.getName().equals(GnagReportTask.TASK_NAME)) {
                return true;
            }
        }

        return false;
    }
}
