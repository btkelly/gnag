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
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.util.HashMap;
import java.util.Map;

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

    private GitHubExtension gitHubExtension;

    @TaskAction
    public void taskAction() {

        GitHubApi gitHubApi = new GitHubApi(gitHubExtension);

        GitHubPullRequest pullRequestDetails = gitHubApi.getPullRequestDetails();

        String prSha = pullRequestDetails.getHead().getSha();

        gitHubApi.postUpdatedGitHubStatus(GitHubStatusType.PENDING, prSha);

        Object projectStatus = getProject().getStatus();

        if (projectStatus instanceof CheckStatus) {
            CheckStatus checkStatus = (CheckStatus) projectStatus;
            gitHubApi.postGitHubComment(checkStatus.getComment());
            gitHubApi.postUpdatedGitHubStatus(checkStatus.getGitHubStatusType(), prSha);
        } else {
            gitHubApi.postUpdatedGitHubStatus(GitHubStatusType.ERROR, prSha);
        }
    }

    public void setGitHubExtension(GitHubExtension gitHubExtension) {
        this.gitHubExtension = gitHubExtension;
    }
}
