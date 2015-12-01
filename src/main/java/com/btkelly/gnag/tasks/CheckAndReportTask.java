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

import com.btkelly.gnag.GnagPluginExtension;
import com.btkelly.gnag.api.GitHubApi;
import com.btkelly.gnag.api.GitHubApi.Status;
import com.btkelly.gnag.models.ViolationComment;
import com.btkelly.gnag.reporters.CheckstyleReporter;
import com.btkelly.gnag.reporters.CommentReporter;
import com.btkelly.gnag.reporters.FindbugsReporter;
import com.btkelly.gnag.reporters.PMDReporter;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class CheckAndReportTask extends DefaultTask {

    public static void addTask(Project project) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, "checkAndReport");
        taskOptions.put(Task.TASK_TYPE, CheckAndReportTask.class);
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");

        CheckAndReportTask checkAndReportTask = (CheckAndReportTask) project.task(taskOptions, "checkAndReport");
        checkAndReportTask.setProject(project);
    }

    private Project project;

    @Override
    public void setProject(Project project) {
        this.project = project;
    }

    @TaskAction
    public void taskAction() {

        GnagPluginExtension gnagPluginExtension = GnagPluginExtension.getExtension(project);

        if (!gnagPluginExtension.hasValidConfig()) {
            throw new GradleException("You must supply gitHubRepoName, gitHubAuthToken, and gitHubIssueNumber for the Gnag plugin to function.");
        }

        boolean failBuildOnError = gnagPluginExtension.getFailBuildOnError();

        List<CommentReporter> reporters = loadReporters();

        ViolationComment violationComment = buildViolationComment(reporters, project);

        GitHubApi gitHubApi = GitHubApi.defaultApi(gnagPluginExtension);

        Status status = gitHubApi.postGitHubComment(violationComment.getCommentMessage());

        if (status == Status.FAIL) {
            throw new GradleException("Error sending violation reports: " + violationComment.getCommentMessage());
        } else if (violationComment.isFailBuild() && failBuildOnError) {
            throw new GradleException("One or more comment reporters has forced the build to fail");
        }
    }

    private ViolationComment buildViolationComment(List<CommentReporter> reporters, Project project) {

        //println "Collecting violation reports";

        StringBuilder commentBuilder = new StringBuilder();

        boolean failBuild = false;

        for (int index = 0; index < reporters.size(); index++) {

            CommentReporter githubCommentReporter = reporters.get(index);

            String violationText = githubCommentReporter.textToAppendComment(project);
            commentBuilder.append(violationText);

            if (githubCommentReporter.shouldFailBuild(project)) {
                //println githubCommentReporter.reporterName() + " found violations"
                failBuild = true;
            }

            if (violationText.trim().length() != 0 && index != reporters.size() - 1) {
                commentBuilder.append("\n\n");
            }
        }

        String messageBody;

        if (commentBuilder.length() > 0) {
            messageBody = commentBuilder.toString();
        } else {
            messageBody = "Congrats! No :poop: code found, this PR is safe to merge.";
        }

        //println messageBody

        return new ViolationComment(failBuild, messageBody);
    }

    private List<CommentReporter> loadReporters() {
        //println "Loading reporters..."

        //TODO allow enable / disable reporters
        //TODO allow custom reporters to be loaded
        List<CommentReporter> reporters = new ArrayList<>();

        FindbugsReporter findbugsReporter = new FindbugsReporter();
        reporters.add(findbugsReporter);

        PMDReporter pmdReporter = new PMDReporter();
        reporters.add(pmdReporter);

        CheckstyleReporter checkstyleReporter = new CheckstyleReporter();
        reporters.add(checkstyleReporter);

        //println "Finished loading reporters"
        return reporters;
    }
}
