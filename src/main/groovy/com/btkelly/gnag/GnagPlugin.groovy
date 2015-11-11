/*
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
package com.btkelly.gnag

import com.btkelly.gnag.reporters.CheckstyleReporter
import com.btkelly.gnag.reporters.CommentReporter
import com.btkelly.gnag.reporters.FindbugsReporter
import com.btkelly.gnag.reporters.PMDReporter
import org.gradle.StartParameter
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

/**
 * The main plugin class allowing hooks into the build system. You can use this plugin by adding the following to your
 * build script.
 *
 * buildscript {
 *  repositories {
 *      maven {
 *          jcenter()
 *      }
 *  }
 *  dependencies {
 *      classpath 'com.btkelly:gnag:0.0.4'
 *  }
 * }
 *
 * apply plugin: 'gnag-plugin'
 *
 * This will reports violations of checkstyle, findbugs, and PMD. For easy setup I recommend using com.noveogroup.android.check
 * plugin with this plugin.
 *
 * You can configure the plugin inside the build script or by passing command line properties of the same name.
 *
 * gnag {
 *     gitHubRepoName "user/repo"
 *     gitHubAuthToken "12312n3j12n3jk1"
 *     gitHubIssueNumber "11"
 *     failBuildOnError true
 * }
 *
 */
class GnagPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        Gradle gradle = project.getGradle();

        StartParameter startParameter = gradle.getStartParameter();

        if (startParameter.isOffline()) {
            println "Build is running offline. Reports will not be collected";
        } else {
            addCheckAndReportTask(project);
        }
    }

    private static void addCheckAndReportTask(Project project) {

        GnagPluginExtension.loadExtension(project);

        project.task("checkAndReport").dependsOn('check') << {

            GnagPluginExtension gnagPluginExtension = project.gnag;

            if (!gnagPluginExtension.hasValidConfig(project)) {
                throw new GradleException("You must supply gitHubRepoName, gitHubAuthToken, and gitHubIssueNumber for the Gnag plugin to function.");
            }

            ArrayList<CommentReporter> reporters = loadReporters();

            def (String commentBody, boolean failBuild) = buildViolationComment(reporters, project);

            sendViolationReport(gnagPluginExtension, project, commentBody, failBuild);
        }
    }

    private
    static void sendViolationReport(GnagPluginExtension gnagPluginExtension, Project project, String commentBody, boolean failBuild) {

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.github.com/repos/" + gnagPluginExtension.getGitHubRepoName(project) + "/issues/" + gnagPluginExtension.getGitHubIssueNumber(project) + "/comments").openConnection();
        httpURLConnection.setRequestProperty("Authorization", "token " + gnagPluginExtension.getGitHubAuthToken(project));
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);

        println "Sending violation reports";

        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        dataOutputStream.writeBytes(commentBody);
        dataOutputStream.flush();
        dataOutputStream.close();

        int statusCode = httpURLConnection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            println "Violation reports sent";

            if (failBuild && gnagPluginExtension.getFailBuildOnError(project)) {
                throw new GradleException("One or more comment reporters has forced the build to fail");
            }

        } else {

            if (gnagPluginExtension.getFailBuildOnError(project)) {
                throw new GradleException("Error sending violation reports, status code: " + statusCode + " " + httpURLConnection.getResponseMessage() + ", URL: " + httpURLConnection.getURL().toString());
            }
        }
    }

    private static List buildViolationComment(ArrayList<CommentReporter> reporters, Project project) {

        println "Collecting violation reports";

        boolean failBuild = false;

        StringBuilder commentBuilder = new StringBuilder();

        for (int index = 0; index < reporters.size(); index++) {

            CommentReporter githubCommentReporter = reporters.get(index);

            String violationText = githubCommentReporter.textToAppendComment(project);
            commentBuilder.append(violationText);

            if (githubCommentReporter.shouldFailBuild(project)) {
                println githubCommentReporter.reporterName() + " found violations"
                failBuild = true;
            }

            if (violationText.trim().length() != 0 && index != reporters.size() - 1) {
                commentBuilder.append("\\n\\n");
            }
        }

        String messageBody;

        if (commentBuilder.length() > 0) {
            messageBody = commentBuilder.toString();
        } else {
            messageBody = "Congrats! No :poop: code found, this PR is safe to merge."
        }

        String commentBody = "{ \"body\" : \"" + messageBody + "\" }";
        [commentBody, failBuild]
    }

    private static ArrayList<CommentReporter> loadReporters() {
        println "Loading reporters..."

        List<CommentReporter> reporters = new ArrayList<>();

        FindbugsReporter findbugsReporter = new FindbugsReporter();
        reporters.add(findbugsReporter);
        println "Loaded " + findbugsReporter.reporterName();

        PMDReporter pmdReporter = new PMDReporter();
        reporters.add(pmdReporter);
        println "Loaded " + pmdReporter.reporterName();

        CheckstyleReporter checkstyleReporter = new CheckstyleReporter();
        reporters.add(checkstyleReporter);
        println "Loaded " + checkstyleReporter.reporterName();

        println "Finished loading reporters"
        reporters
    }
}