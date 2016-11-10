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
import com.btkelly.gnag.api.GitHubApi;
import com.btkelly.gnag.extensions.GitHubExtension;
import com.btkelly.gnag.models.*;
import com.btkelly.gnag.utils.StringUtils;
import com.btkelly.gnag.utils.ViolationFormatter;
import com.btkelly.gnag.utils.ViolationsFormatter;
import com.btkelly.gnag.utils.ViolationsUtil;
import com.github.stkent.githubdiffparser.models.Diff;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.btkelly.gnag.models.GitHubStatusType.*;
import static com.btkelly.gnag.models.Violation.COMPARATOR;
import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagReportTask extends DefaultTask {

    private static final String GLOBAL_TASK_NAME = "gnagReport";

    private static final String REMOTE_SUCCESS_COMMENT_FORMAT_STRING = "Congrats, no :poop: code found%s!";

    public static void addTasksToProject(
            @NotNull final Project project,
            @NotNull final GitHubExtension gitHubExtension,
            @NotNull final Collection<? extends BaseVariant> variants) {

        final List<String> variantGnagReportTaskNames = new ArrayList<>();
        
        variants.forEach(variant -> {
            String variantName = variant.getName();

            Map<String, Object> variantTaskOptions = new HashMap<>();

            variantTaskOptions.put(TASK_TYPE, GnagReportTask.class);
            variantTaskOptions.put(TASK_GROUP, "Verification");
            variantTaskOptions.put(TASK_DEPENDS_ON, GnagCheckTask.getTaskNameForBuildVariant(variant));
            variantTaskOptions.put(TASK_DESCRIPTION, "Runs Gnag on the " + variantName + " build variant, reports results to GitHub, and sets the status of a PR");

            GnagReportTask variantGnagReportTask = (GnagReportTask) project.task(variantTaskOptions, getTaskNameForBuildVariant(variant));
            variantGnagReportTask.dependsOn(GnagCheckTask.getTaskNameForBuildVariant(variant));
            variantGnagReportTask.setGitHubExtension(gitHubExtension);
            
            variantGnagReportTaskNames.add(variantGnagReportTask.getName());
        });

        final Map<String, Object> globalTaskOptions = new HashMap<>();

        globalTaskOptions.put(TASK_GROUP, "Verification");
        globalTaskOptions.put(TASK_DESCRIPTION, "Runs Gnag on all build variants, reports results to GitHub, and sets the status of a PR");
        globalTaskOptions.put(TASK_DEPENDS_ON, variantGnagReportTaskNames);

        project.task(globalTaskOptions, GLOBAL_TASK_NAME);
    }

    @NotNull
    private static String getTaskNameForBuildVariant(@NotNull final BaseVariant variant) {
        String variantName = variant.getName();
        return GLOBAL_TASK_NAME + StringUtils.capitalizeFirstChar(variantName);
    }

    private GitHubApi gitHubApi;
    private String prSha;

    @SuppressWarnings("unused")
    @TaskAction
    public void taskAction() {

        updatePRStatus(PENDING);

        final Object projectStatus = getProject().getStatus();

        if (projectStatus instanceof CheckStatus) {
            final CheckStatus checkStatus = (CheckStatus) projectStatus;
            System.out.println("Project status: " + checkStatus);

            fetchPRShaIfRequired();

            if (checkStatus.getGitHubStatusType() == SUCCESS) {
                final String commitString = prSha != null
                        ? " as of commit " + prSha.substring(0, min(7, prSha.length()))
                        : "";

                gitHubApi.postGitHubPRCommentAsync(String.format(REMOTE_SUCCESS_COMMENT_FORMAT_STRING, commitString));
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
        final Set<Violation> violationsWithAllLocationInformation
                = ViolationsUtil.hasViolationWithAllLocationInformation(violations);

        if (StringUtils.isBlank(prSha) || violationsWithAllLocationInformation.isEmpty()) {
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
                        prSha, 
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
