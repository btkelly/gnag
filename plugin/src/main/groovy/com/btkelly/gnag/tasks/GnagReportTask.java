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

import static com.btkelly.gnag.models.GitHubStatusType.ERROR;
import static com.btkelly.gnag.models.GitHubStatusType.PENDING;
import static com.btkelly.gnag.models.GitHubStatusType.SUCCESS;
import static com.btkelly.gnag.models.Violation.COMPARATOR;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.btkelly.gnag.GnagPlugin;
import com.btkelly.gnag.api.GitHubApi;
import com.btkelly.gnag.extensions.GitHubExtension;
import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.models.CheckStatus;
import com.btkelly.gnag.models.GitHubPRDetails;
import com.btkelly.gnag.models.GitHubStatusType;
import com.btkelly.gnag.models.PRLocation;
import com.btkelly.gnag.models.Violation;
import com.btkelly.gnag.utils.ProjectHelper;
import com.btkelly.gnag.utils.ViolationFormatter;
import com.btkelly.gnag.utils.ViolationsFormatter;
import com.btkelly.gnag.utils.ViolationsUtil;
import com.github.stkent.githubdiffparser.models.Diff;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagReportTask extends DefaultTask {

  static final String TASK_NAME = "gnagReport";
  private static final String REMOTE_SUCCESS_COMMENT_FORMAT_STRING = "Congrats, no :poop: code found in the **%s** module%s!";
  private boolean commentInline;
  private boolean commentOnSuccess;
  private GitHubApi gitHubApi;
  private String prSha;

  public static void addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension) {
    Map<String, Object> taskOptions = new HashMap<>();

    taskOptions.put(Task.TASK_NAME, TASK_NAME);
    taskOptions.put(TASK_TYPE, GnagReportTask.class);
    taskOptions.put(TASK_GROUP, "Verification");
    taskOptions.put(TASK_DEPENDS_ON, "check");
    taskOptions.put(TASK_DESCRIPTION,
        "Runs Gnag and generates a report to publish to GitHub and set the status of a PR");

    Project project = projectHelper.getProject();

    GnagReportTask gnagReportTask = (GnagReportTask) project.task(taskOptions, TASK_NAME);
    if (gnagPluginExtension.shouldRunCheckOnReport()) {
      gnagReportTask.dependsOn(GnagCheckTask.TASK_NAME);
    }
    gnagReportTask.setGitHubExtension(gnagPluginExtension.github);
  }

  @SuppressWarnings("unused")
  @TaskAction
  public void taskAction() {
    updatePRStatus(PENDING);

    final Object projectStatus = getProject().getStatus();

    if (projectStatus instanceof CheckStatus) {
      final CheckStatus checkStatus = (CheckStatus) projectStatus;
      getLogger().info("Project status: " + checkStatus);

      if (checkStatus.getGitHubStatusType() == SUCCESS) {
        if (commentOnSuccess) {
          final String commitString = getPRSha() != null ? " as of commit " + getPRSha() : "";

          gitHubApi.postGitHubPRComment(
              String.format(REMOTE_SUCCESS_COMMENT_FORMAT_STRING, getProject().getName(),
                  commitString));
        }
      } else {
        postViolationComments(checkStatus.getViolations());
      }

      updatePRStatus(checkStatus.getGitHubStatusType());
    } else {
      getLogger().error("Project status is not instanceof Check Status");
      updatePRStatus(ERROR);
    }
  }

  @Override
  public Logger getLogger() {
    return Logging.getLogger(GnagPlugin.class);
  }

  private void setGitHubExtension(GitHubExtension gitHubExtension) {
    commentInline = gitHubExtension.isCommentInline();
    commentOnSuccess = gitHubExtension.isCommentOnSuccess();
    gitHubApi = new GitHubApi(gitHubExtension, getLogger());
  }

  private String getPRSha() {
    if (StringUtils.isBlank(prSha)) {
      getLogger().debug("getPRSha: fetching...");
      GitHubPRDetails pullRequestDetails = gitHubApi.getPRDetails();
      prSha = pullRequestDetails.getHead().getSha();
    }

    getLogger().debug("getPRSha: " + prSha);

    return prSha;
  }

  private void updatePRStatus(GitHubStatusType gitHubStatusType) {
    getLogger().debug("Updating PR Status to: " + gitHubStatusType);
    if (StringUtils.isNotBlank(getPRSha())) {
      gitHubApi.postUpdatedGitHubStatus(gitHubStatusType, getProject().getName(), getPRSha());
    }
  }

  private void postViolationComments(@NotNull final Set<Violation> violations) {
    final Set<Violation> violationsWithAllLocationInformation
        = ViolationsUtil.hasViolationWithAllLocationInformation(violations);

    if (StringUtils.isBlank(getPRSha()) ||
        violationsWithAllLocationInformation.isEmpty() ||
        !commentInline) {

      gitHubApi
          .postGitHubPRComment(ViolationsFormatter.getHtmlStringForAggregatedComment(violations));
      return;
    }

    final List<Diff> diffs = gitHubApi.getPRDiffs();

    if (diffs.isEmpty()) {
      gitHubApi
          .postGitHubPRComment(ViolationsFormatter.getHtmlStringForAggregatedComment(violations));
      return;
    }

    final Map<Violation, PRLocation> violationPRLocationMap
        = ViolationsUtil.getPRLocationsForViolations(violations, diffs);

    final List<Violation> violationsWithValidLocationInfo = new ArrayList<>();
    final Set<Violation> violationsWithMissingOrInvalidLocationInfo = new HashSet<>();

    for (final Map.Entry<Violation, PRLocation> entry : violationPRLocationMap.entrySet()) {
      final Violation violation = entry.getKey();
      final PRLocation prLocation = entry.getValue();

      if (prLocation != null) {
        violationsWithValidLocationInfo.add(violation);
      } else {
        violationsWithMissingOrInvalidLocationInfo.add(violation);
      }
    }

    violationsWithValidLocationInfo.sort(COMPARATOR);

    violationsWithValidLocationInfo.forEach(violation ->
        gitHubApi.postGitHubInlineComment(
            ViolationFormatter.getHtmlStringForInlineComment(violation),
            getPRSha(),
            violationPRLocationMap.get(violation)));

    if (!violationsWithMissingOrInvalidLocationInfo.isEmpty()) {
      try {
        /*
         * Try to post the aggregate comment _strictly after_ all individual comments. GitHub seems to round
         * post times to the nearest second, so delaying by one whole second should be sufficient here.
         */
        Thread.sleep(SECONDS.toMillis(1));
      } catch (final InterruptedException e) {
        e.printStackTrace();
      }

      gitHubApi.postGitHubPRComment(
          ViolationsFormatter.getHtmlStringForAggregatedComment(
              violationsWithMissingOrInvalidLocationInfo));
    }
  }

}