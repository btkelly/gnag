/**
 * Copyright 2015 Bryan Kelly
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

import com.btkelly.gnag.actions.StatusPendingAction;
import com.btkelly.gnag.api.GitHubApi;
import com.btkelly.gnag.api.GitHubApi.Status;
import com.btkelly.gnag.models.ViolationComment;
import com.btkelly.gnag.models.github.GitHubStatusType;
import com.btkelly.gnag.utils.Logger;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class CheckAndReportTask extends BaseCheckTask {

    private static final String TASK_NAME = "checkAndReport";

    public static void addTask(Project project) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(Task.TASK_TYPE, CheckAndReportTask.class);
        taskOptions.put(Task.TASK_GROUP, "Verification");
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag and generates a report to publish to Github and set the status of a PR");

        project.getTasks()
                .findByName("check")
                .doFirst(new StatusPendingAction());

        project.task(taskOptions, TASK_NAME);
    }

    @TaskAction
    public void taskAction() {

        if (StatusPendingAction.isPendingActionCompleted()) {

            ViolationComment violationComment = buildViolationComment();

            GitHubApi gitHubApi = GitHubApi.defaultApi(getGnagPluginExtension());

            Status status = gitHubApi.postGitHubComment(violationComment.getCommentMessage());

            if (status == Status.FAIL) {
                gitHubApi.postUpdatedGitHubStatus(GitHubStatusType.ERROR, StatusPendingAction.getIssueSha());
                Logger.logE("Error sending violation reports: " + violationComment.getCommentMessage());
            } else if (violationComment.isFailBuild() && failBuildOnError()) {
                gitHubApi.postUpdatedGitHubStatus(GitHubStatusType.FAILURE, StatusPendingAction.getIssueSha());
                Logger.logE("One or more comment reporters has forced the build to fail");
            } else {
                gitHubApi.postUpdatedGitHubStatus(GitHubStatusType.SUCCESS, StatusPendingAction.getIssueSha());
            }
        }

    }
}
