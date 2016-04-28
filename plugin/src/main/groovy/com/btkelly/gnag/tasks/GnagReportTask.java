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
import com.btkelly.gnag.models.CheckStatus;
import com.btkelly.gnag.models.GitHubPullRequest;
import com.btkelly.gnag.models.GitHubStatusType;
import org.apache.commons.lang.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.util.HashMap;
import java.util.Map;

import static com.btkelly.gnag.models.GitHubStatusType.ERROR;
import static com.btkelly.gnag.models.GitHubStatusType.PENDING;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagReportTask extends DefaultTask {

    public static final String TASK_NAME = "gnagReport";

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

        Object projectStatus = getProject().getStatus();

        if (projectStatus instanceof CheckStatus) {
            CheckStatus checkStatus = (CheckStatus) projectStatus;

            System.out.println("Project status: " + checkStatus);

            gitHubApi.postGitHubComment(checkStatus.getComment());
            updatePRStatus(checkStatus.getGitHubStatusType());
        } else {
            System.out.println("Project status is not instanceof Check Status");
            updatePRStatus(ERROR);
        }
    }

    public void setGitHubExtension(GitHubExtension gitHubExtension) {
        this.gitHubApi = new GitHubApi(gitHubExtension);
    }

    private void updatePRStatus(GitHubStatusType gitHubStatusType) {

        if (StringUtils.isBlank(prSha)) {
            GitHubPullRequest pullRequestDetails = gitHubApi.getPullRequestDetails();

            if (pullRequestDetails != null && pullRequestDetails.getHead() != null) {
                prSha = pullRequestDetails.getHead().getSha();
            }
        }

        if (StringUtils.isNotBlank(prSha)) {
            gitHubApi.postUpdatedGitHubStatus(gitHubStatusType, prSha);
        }
    }
}
