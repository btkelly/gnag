package com.btkelly.gnag

import com.btkelly.gnag.reporters.CommentReporter
import com.btkelly.gnag.reporters.PMDReporter
import org.gradle.StartParameter
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

/**
 * Created by bobbake4 on 10/23/15.
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

        println "Loading reporters..."

        List<CommentReporter> reporters = new ArrayList<>();

        //TODO load all reporters from config
        PMDReporter pmdReporter = new PMDReporter();
        reporters.add(pmdReporter);
        println "Loaded " + pmdReporter.reporterName();

        println "Finished loading reporters..."

        project.task("checkAndReport").dependsOn('check') << {

            println "Collecting violation reports";

            boolean failBuild = false;

            StringBuilder commentBuilder = new StringBuilder();

            for (CommentReporter githubCommentReporter : reporters) {
                commentBuilder.append(githubCommentReporter.textToAppendComment(project));

                if (githubCommentReporter.shouldFailBuild(project)) {
                    failBuild = true;
                }
            }

            String commentBody = "{ \"body\" : \"" + commentBuilder.toString() + "\" }";

            GnagPluginExtension gnagPluginExtension = project.gnag;

            if (!gnagPluginExtension.hasValidConfig()) {
                throw new GradleException("You must supply gitHubRepoName, gitHubAuthToken, and gitHubIssueNumber for the Gnag plugin to function.");
            }

            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.github.com/repos/" + gnagPluginExtension.gitHubRepoName + "/issues/" + gnagPluginExtension.gitHubIssueNumber + "/comments").openConnection();
            httpURLConnection.setRequestProperty("Authorization", "token " + gnagPluginExtension.gitHubAuthToken);
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

                if (failBuild && gnagPluginExtension.failBuildOnError) {
                    throw new GradleException("One or more comment reporters has forced the build to fail");
                }

            } else {

                if (gnagPluginExtension.failBuildOnError) {
                    throw new GradleException("Error sending violation reports, status code: " + statusCode + " " + httpURLConnection.getResponseMessage() + ", URL: " + httpURLConnection.getURL().toString());
                }
            }
        }
    }
}