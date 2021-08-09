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

import static com.btkelly.gnag.models.GitHubStatusType.FAILURE;
import static com.btkelly.gnag.utils.ReportWriter.REPORT_FILE_NAME;

import com.btkelly.gnag.GnagPlugin;
import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.models.CheckStatus;
import com.btkelly.gnag.models.Violation;
import com.btkelly.gnag.reporters.AndroidLintViolationDetector;
import com.btkelly.gnag.reporters.BaseExecutedViolationDetector;
import com.btkelly.gnag.reporters.CheckstyleViolationDetector;
import com.btkelly.gnag.reporters.DetektViolationDetector;
import com.btkelly.gnag.reporters.FindbugsViolationDetector;
import com.btkelly.gnag.reporters.KtlintViolationDetector;
import com.btkelly.gnag.reporters.PMDViolationDetector;
import com.btkelly.gnag.reporters.ViolationDetector;
import com.btkelly.gnag.utils.ProjectHelper;
import com.btkelly.gnag.utils.ReportWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.StopExecutionException;
import org.gradle.api.tasks.TaskAction;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagCheckTask extends DefaultTask {

  public static final String KTLINT_TOOL_VERSION = "0.39.0";
  public static final String DETEKT_TOOL_VERSION = "1.13.1";
  public static final String PMD_TOOL_VERSION = "6.22.0";

  static final String TASK_NAME = "gnagCheck";
  private final ProjectHelper projectHelper = new ProjectHelper(getProject());
  private GnagPluginExtension gnagPluginExtension;
  private final List<ViolationDetector> violationDetectors = new ArrayList<>();

  public static void addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension) {
    Map<String, Object> taskOptions = new HashMap<>();

    taskOptions.put(Task.TASK_NAME, TASK_NAME);
    taskOptions.put(Task.TASK_TYPE, GnagCheckTask.class);
    taskOptions.put(Task.TASK_GROUP, "Verification");
    taskOptions.put(Task.TASK_DEPENDS_ON, "check");
    taskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag checks and generates an HTML report");

    Project project = projectHelper.getProject();

    GnagCheckTask gnagCheckTask = (GnagCheckTask) project.task(taskOptions, TASK_NAME);
    gnagCheckTask.setGnagPluginExtension(gnagPluginExtension);

    /*
    if (gnagPluginExtension.checkstyle.isEnabled() && projectHelper.hasJavaSourceFiles()) {
      gnagCheckTask.violationDetectors
          .add(new CheckstyleViolationDetector(project, gnagPluginExtension.checkstyle));
    }
     */

    ViolationDetector pmdViolationDetector = PMDViolationDetector.configure(projectHelper, gnagCheckTask, gnagPluginExtension);
    if (pmdViolationDetector != null) {
      gnagCheckTask.violationDetectors.add(pmdViolationDetector);
    }

    ViolationDetector ktlintViolationDetector = KtlintViolationDetector.configure(projectHelper, gnagCheckTask, gnagPluginExtension);
    if (ktlintViolationDetector != null) {
      gnagCheckTask.violationDetectors.add(ktlintViolationDetector);
    }

    ViolationDetector detektViolationDetector = DetektViolationDetector.configure(projectHelper, gnagCheckTask, gnagPluginExtension);
    if (detektViolationDetector != null) {
      gnagCheckTask.violationDetectors.add(detektViolationDetector);
    }

    ViolationDetector androidLintViolationDetector = AndroidLintViolationDetector.configure(projectHelper, gnagCheckTask, gnagPluginExtension);
    if (androidLintViolationDetector != null) {
      gnagCheckTask.violationDetectors.add(androidLintViolationDetector);
    }
  }

  @SuppressWarnings("unused")
  @TaskAction
  public void taskAction() {
    if (gnagPluginExtension.isEnabled()) {
      executeGnagCheck();
    }
  }

  private Logger getGnagLogger() {
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

      getGnagLogger().lifecycle(
          violationDetector.name() + " detected " + detectedViolations.size() + " violations.");
    });

    final File reportsDir = projectHelper.getReportsDir();

    if (allDetectedViolations.isEmpty()) {
      ReportWriter.deleteLocalReportFiles(reportsDir, getGnagLogger());

      getProject().setStatus(CheckStatus.getSuccessfulCheckStatus());

      getGnagLogger().lifecycle("Congrats, no poop code found!");
    } else {
      ReportWriter.writeLocalReportFiles(allDetectedViolations, reportsDir, getGnagLogger());

      getProject().setStatus(new CheckStatus(FAILURE, allDetectedViolations));

      File reportFile = new File(reportsDir, REPORT_FILE_NAME);

      final String failedMessage= "One or more violation detectors has found violations. Check the report at " +
              "file://" + reportFile.getAbsolutePath();

      if (gnagPluginExtension.shouldFailOnError() && !taskExecutionGraphIncludesGnagReport()) {
        throw new GradleException(failedMessage);
      } else {
        getGnagLogger().lifecycle(failedMessage);
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
