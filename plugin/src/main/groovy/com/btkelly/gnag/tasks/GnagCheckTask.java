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

import com.android.build.gradle.api.BaseVariant;
import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.models.CheckStatus;
import com.btkelly.gnag.models.Violation;
import com.btkelly.gnag.reporters.*;
import com.btkelly.gnag.utils.ReportHelper;
import com.btkelly.gnag.utils.ReportWriter;
import com.btkelly.gnag.utils.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.StopExecutionException;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

import static com.btkelly.gnag.models.GitHubStatusType.FAILURE;
import static com.btkelly.gnag.utils.ReportWriter.REPORT_FILE_NAME;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagCheckTask extends DefaultTask {

    private static final String GLOBAL_TASK_NAME = "gnagCheck";

    public static void addTasksToProject(
            @NotNull final Project project,
            @NotNull final GnagPluginExtension gnagPluginExtension,
            @NotNull final Collection<? extends BaseVariant> variants) {

        final List<String> variantGnagCheckTaskNames = new ArrayList<>();

        variants.forEach(variant -> {
            String variantName = variant.getName();
            String capitalizedVariantName = StringUtils.capitalizeFirstChar(variantName);

            Map<String, Object> variantTaskOptions = new HashMap<>();

            variantTaskOptions.put(Task.TASK_TYPE, GnagCheckTask.class);
            variantTaskOptions.put(Task.TASK_GROUP, "Verification");
            variantTaskOptions.put(Task.TASK_DEPENDS_ON, "lint" + capitalizedVariantName);
            variantTaskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag checks and generates an HTML report for the " + variantName + " build variant.");

            GnagCheckTask variantGnagCheckTask = (GnagCheckTask) project.task(variantTaskOptions, getTaskNameForBuildVariant(variant));
            variantGnagCheckTask.setGnagPluginExtension(gnagPluginExtension);
            variantGnagCheckTask.violationDetectors.add(new CheckstyleViolationDetector(project, gnagPluginExtension.checkstyle));
            variantGnagCheckTask.violationDetectors.add(new PMDViolationDetector(project, gnagPluginExtension.pmd));
            variantGnagCheckTask.violationDetectors.add(new FindbugsViolationDetector(project, gnagPluginExtension.findbugs));
            variantGnagCheckTask.violationDetectors.add(new AndroidLintViolationDetector(project, gnagPluginExtension.androidLint));

            variantGnagCheckTaskNames.add(variantGnagCheckTask.getName());
        });

        final Map<String, Object> globalTaskOptions = new HashMap<>();

        globalTaskOptions.put(Task.TASK_GROUP, "Verification");
        globalTaskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag checks and generates an HTML report for all build variants.");
        globalTaskOptions.put(Task.TASK_DEPENDS_ON, variantGnagCheckTaskNames);

        project.task(globalTaskOptions, GLOBAL_TASK_NAME);
    }
    
    @NotNull
    public static String getTaskNameForBuildVariant(@NotNull final BaseVariant variant) {
        String variantName = variant.getName();
        return GLOBAL_TASK_NAME + StringUtils.capitalizeFirstChar(variantName);
    }

    private final List<ViolationDetector> violationDetectors = new ArrayList<>();
    private final ReportHelper reportHelper = new ReportHelper(getProject());

    private GnagPluginExtension gnagPluginExtension;

    @SuppressWarnings("unused")
    @TaskAction
    public void taskAction() {
        if (gnagPluginExtension.isEnabled()) {
            executeGnagCheck();
        }
    }

    private void executeGnagCheck() {
        final Set<Violation> allDetectedViolations = new HashSet<>();

        violationDetectors
                .stream()
                .filter(ViolationDetector::isEnabled)
                .forEach(violationDetector -> {

                        if (violationDetector instanceof BaseExecutedViolationDetector) {
                            ((BaseExecutedViolationDetector) violationDetector).executeReporter();
                        }

                        final List<Violation> detectedViolations = violationDetector.getDetectedViolations();
                        allDetectedViolations.addAll(detectedViolations);

                        System.out.println(
                                violationDetector.name() + " detected " + detectedViolations.size() + " violations.");
                });

        final File reportsDir = reportHelper.getReportsDir();

        if (allDetectedViolations.isEmpty()) {
            ReportWriter.deleteLocalReportFiles(reportsDir);
            
            getProject().setStatus(CheckStatus.getSuccessfulCheckStatus());

            System.out.println("Congrats, no poop code found!");
        } else {
            ReportWriter.writeLocalReportFiles(allDetectedViolations, reportsDir);
            
            getProject().setStatus(new CheckStatus(FAILURE, allDetectedViolations));

            final String failedMessage
                    = "One or more violation detectors has found violations. Check the report at " 
                    + reportsDir
                    + File.separatorChar
                    + REPORT_FILE_NAME + " for details.";
            
            if (gnagPluginExtension.shouldFailOnError() && !taskExecutionGraphIncludesGnagReport()) {
                throw new GradleException(failedMessage);
            } else {
                System.out.println(failedMessage);
                throw new StopExecutionException(failedMessage);
            }
        }
    }

    private void setGnagPluginExtension(GnagPluginExtension gnagPluginExtension) {
        this.gnagPluginExtension = gnagPluginExtension;
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
