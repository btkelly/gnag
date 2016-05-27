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
import com.btkelly.gnag.utils.ViolationFormatter;
import com.btkelly.gnag.utils.ViolationsFormatter;
import org.apache.commons.lang.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.btkelly.gnag.models.GitHubStatusType.*;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagReportTask extends DefaultTask {

    public static final String TASK_NAME = "gnagReport";
    private static final String REMOTE_SUCCESS_COMMENT = "Congrats, no :poop: code found! This PR is safe to merge.";

    public static void addTask(Project project, GitHubExtension gitHubExtension) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(Task.TASK_TYPE, GnagReportTask.class);
        taskOptions.put(Task.TASK_GROUP, "Verification");
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag and generates a report to publish to Github and set the status of a PR");

        GnagReportTask gnagReportTask = (GnagReportTask) project.task(taskOptions, TASK_NAME);
        gnagReportTask.dependsOn(GnagCheck.TASK_NAME);
        gnagReportTask.setGitHubExtension(gitHubExtension);
    }

    private GitHubApi gitHubApi;
    private String prSha;

    @TaskAction
    public void taskAction() {

        updatePRStatus(PENDING);

        final Object projectStatus = getProject().getStatus();

        if (projectStatus instanceof CheckStatus) {
            final CheckStatus checkStatus = (CheckStatus) projectStatus;
            System.out.println("Project status: " + checkStatus);

            // TODO: retry a couple times if this fails; if we can't grab the PR sha, we will
            // need to fall back to posting all violations in an aggregate comment
            fetchPRShaIfRequired();
            
            if (checkStatus.getGitHubStatusType() == SUCCESS) {
                gitHubApi.postGitHubIssueCommentAsync(REMOTE_SUCCESS_COMMENT);
            } else {
                postViolationComments(checkStatus.getViolations());
            }

            updatePRStatus(checkStatus.getGitHubStatusType());
        } else {
            System.out.println("Project status is not instanceof Check Status");
            updatePRStatus(ERROR);
        }
    }

    public void setGitHubExtension(GitHubExtension gitHubExtension) {
        this.gitHubApi = new GitHubApi(gitHubExtension);
    }

    private void fetchPRShaIfRequired() {
        if (StringUtils.isBlank(prSha)) {
            GitHubPRDetails pullRequestDetails = gitHubApi.getPRDetailsSync();

            if (pullRequestDetails != null && pullRequestDetails.getHead() != null) {
                prSha = pullRequestDetails.getHead().getSha();
            }
        }
    }

    private void updatePRStatus(GitHubStatusType gitHubStatusType) {
        if (StringUtils.isNotBlank(prSha)) {
            gitHubApi.postUpdatedGitHubStatusAsync(gitHubStatusType, prSha);
        }
    }

    private void postViolationComments(@NotNull final Set<Violation> violations) {
        final Set<Violation> violationsWithAllLocationInfo = getViolationsWithAllLocationInfo(violations);

        if (StringUtils.isBlank(prSha) || violationsWithAllLocationInfo.isEmpty()) {
            gitHubApi.postGitHubIssueCommentAsync(ViolationsFormatter.getHtmlStringForAggregatedComment(violations));
            return;
        }
        
        final GitHubPRDiffWrapper diffWrapper = gitHubApi.getPRDiffWrapperSync();
        
        if (diffWrapper == null) {
            gitHubApi.postGitHubIssueCommentAsync(ViolationsFormatter.getHtmlStringForAggregatedComment(violations));
            return;
        }

        for (final Violation violation : getViolationsWithValidLocationInfo(violations, diffWrapper)) {
            //noinspection ConstantConditions
            gitHubApi.postGitHubPRCommentAsync(
                    ViolationFormatter.getHtmlStringForInlineComment(violation),
                    prSha,
                    violation.getRelativeFilePath(),
                    violation.getFileLineNumber()); // todo: this needs to change
        }
        
        gitHubApi.postGitHubIssueCommentAsync(
                ViolationsFormatter.getHtmlStringForAggregatedComment(
                        getViolationsWithMissingOrInvalidLocationInfo(violations, diffWrapper)));
    }
    
    private Set<Violation> getViolationsWithAllLocationInfo(@NotNull final Set<Violation> violations) {
        return violations
                .stream()
                .filter(Violation::hasAllLocationInfo)
                .collect(Collectors.toSet());
    }

    private Set<Violation> getViolationsWithValidLocationInfo(
            @NotNull final Set<Violation> violations,
            @NotNull final GitHubPRDiffWrapper diffWrapper) {

        return new HashSet<>();
        
//        return getViolationsWithAllLocationInfo(violations)
//                .stream()
//                // .filter() TODO
//                .collect(Collectors.toSet());
    }

    private Set<Violation> getViolationsWithMissingOrInvalidLocationInfo(
            @NotNull final Set<Violation> violations,
            @NotNull final GitHubPRDiffWrapper diffWrapper) {
        
        final Set<Violation> result = new HashSet<>(violations);
        result.removeAll(getViolationsWithValidLocationInfo(violations, diffWrapper));
        return result;
    }
    
}
