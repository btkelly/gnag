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

import com.btkelly.gnag.api.GitHubApi;
import com.btkelly.gnag.extensions.GitHubExtension;
import com.btkelly.gnag.models.*;
import com.btkelly.gnag.models.GitHubStatusType;
import com.btkelly.gnag.utils.ProjectHelper;
import com.btkelly.gnag.utils.ViolationFormatter;
import com.btkelly.gnag.utils.ViolationsFormatter;
import com.btkelly.gnag.utils.ViolationsUtil;
import com.github.stkent.githubdiffparser.models.Diff;
import org.apache.commons.lang.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.btkelly.gnag.models.GitHubStatusType.*;
import static com.btkelly.gnag.models.Violation.COMPARATOR;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagReportTask extends DefaultTask {

    static final String TASK_NAME = "gnagReport";
    private static final String REMOTE_SUCCESS_COMMENT_FORMAT_STRING = "Congrats, no :poop: code found in the **%s** module%s!";

    public static void addTask(ProjectHelper projectHelper, GitHubExtension gitHubExtension) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(TASK_TYPE, GnagReportTask.class);
        taskOptions.put(TASK_GROUP, "Verification");
        taskOptions.put(TASK_DEPENDS_ON, "check");
        taskOptions.put(TASK_DESCRIPTION, "Runs Gnag and generates a report to publish to GitHub and set the status of a PR");

        Project project = projectHelper.getProject();

        GnagReportTask gnagReportTask = (GnagReportTask) project.task(taskOptions, TASK_NAME);
        gnagReportTask.dependsOn(GnagCheckTask.TASK_NAME);
        gnagReportTask.setGitHubExtension(gitHubExtension);
    }

    private boolean commentInline;
    private boolean commentOnSuccess;
    private GitHubApi gitHubApi;
    private String prSha;

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

                    gitHubApi.postGitHubPRCommentAsync(
                            String.format(REMOTE_SUCCESS_COMMENT_FORMAT_STRING, getProject().getName(), commitString));
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

    private void setGitHubExtension(GitHubExtension gitHubExtension) {
        commentInline = gitHubExtension.isCommentInline();
        commentOnSuccess = gitHubExtension.isCommentOnSuccess();
        gitHubApi = new GitHubApi(gitHubExtension);
    }

    private String getPRSha() {
        if (StringUtils.isBlank(prSha)) {
            GitHubPRDetails pullRequestDetails = gitHubApi.getPRDetailsSync();

            if (pullRequestDetails.getHead() != null) {
                prSha = pullRequestDetails.getHead().getSha();
            } else {
                getLogger().error("HEAD is null, unable to fetch PR Sha");
            }
        }

        return prSha;
    }

    private void updatePRStatus(GitHubStatusType gitHubStatusType) {
        if (StringUtils.isNotBlank(getPRSha())) {
            gitHubApi.postUpdatedGitHubStatusAsync(gitHubStatusType, getProject().getName(), getPRSha());
        }
    }

    private void postViolationComments(@NotNull final Set<Violation> violations) {
        final Set<Violation> violationsWithAllLocationInformation
                = ViolationsUtil.hasViolationWithAllLocationInformation(violations);

        if (StringUtils.isBlank(getPRSha()) ||
                violationsWithAllLocationInformation.isEmpty() ||
                !commentInline) {

            gitHubApi.postGitHubPRCommentAsync(ViolationsFormatter.getHtmlStringForAggregatedComment(violations));
            return;
        }

        final List<Diff> diffs = gitHubApi.getPRDiffsSync();

        if (diffs.isEmpty()) {
            gitHubApi.postGitHubPRCommentAsync(ViolationsFormatter.getHtmlStringForAggregatedComment(violations));
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
                gitHubApi.postGitHubInlineCommentSync(
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

            gitHubApi.postGitHubPRCommentAsync(
                    ViolationsFormatter.getHtmlStringForAggregatedComment(
                            violationsWithMissingOrInvalidLocationInfo));
        }
    }

}