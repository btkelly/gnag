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
package com.btkelly.gnag.actions;

import com.btkelly.gnag.GnagPluginExtension;
import com.btkelly.gnag.api.GitHubApi;
import com.btkelly.gnag.models.github.GitHubPullRequest;
import com.btkelly.gnag.models.github.GitHubStatusType;
import com.btkelly.gnag.utils.Logger;
import org.gradle.StartParameter;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.invocation.Gradle;

/**
 * Created by bobbake4 on 12/3/15.
 */
public class StatusPendingAction implements Action<Task> {

    private static boolean pendingActionCompleted;
    private static String issueSha;

    public static boolean isPendingActionCompleted() {
        return pendingActionCompleted;
    }

    public static String getIssueSha() {
        return issueSha;
    }

    @Override
    public void execute(Task task) {

        pendingActionCompleted = false;

        Gradle gradle = task.getProject().getGradle();

        StartParameter startParameter = gradle.getStartParameter();

        if (startParameter.isOffline()) {

            Logger.logError("Build is running offline, reports will not be collected. Run \"checkLocal\" to run checks without report");

        } else {

            GnagPluginExtension gnagPluginExtension = GnagPluginExtension.getExtension(task.getProject());

            if (gnagPluginExtension.hasValidConfig()) {

                GitHubApi gitHubApi = GitHubApi.defaultApi(gnagPluginExtension);

                GitHubPullRequest gitHubPullRequest = gitHubApi.getPullRequestDetails();

                if (gitHubPullRequest != null) {

                    issueSha = gitHubPullRequest.getHead().getSha();

                    gitHubApi.postUpdatedGitHubStatus(GitHubStatusType.PENDING, issueSha);

                    pendingActionCompleted = true;

                    Logger.logInfo("GitHub pending action complete");

                } else {
                    Logger.logError("Error fetching pull request details");
                }

            } else {
                Logger.logError("You must supply gitHubRepoName, gitHubAuthToken, and gitHubIssueNumber for the Gnag plugin to report. Run \"checkLocal\" to run checks without report");
            }
        }
    }

}
