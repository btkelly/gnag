package com.btkelly.gnag.tasks;

import com.btkelly.gnag.GnagPluginExtension;
import com.btkelly.gnag.models.ViolationComment;
import com.btkelly.gnag.reporters.CheckstyleReporter;
import com.btkelly.gnag.reporters.CommentReporter;
import com.btkelly.gnag.reporters.FindbugsReporter;
import com.btkelly.gnag.reporters.PMDReporter;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

        GnagPluginExtension gnagPluginExtension = (GnagPluginExtension) project.getExtensions().getByName("gnag");

        if (!gnagPluginExtension.hasValidConfig()) {
            throw new GradleException("You must supply gitHubRepoName, gitHubAuthToken, and gitHubIssueNumber for the Gnag plugin to function.");
        }

        String repoName = gnagPluginExtension.getGitHubRepoName();
        String issueNumber = gnagPluginExtension.getGitHubIssueNumber();
        String authToken = gnagPluginExtension.getGitHubAuthToken();
        boolean failBuildOnError = gnagPluginExtension.getFailBuildOnError();

        List<CommentReporter> reporters = loadReporters();

        ViolationComment violationComment = buildViolationComment(reporters, project);

        boolean issueCommentSuccessful;

        try {
            issueCommentSuccessful = sendViolationReport(authToken, repoName, issueNumber, violationComment.getCommentJson());
        } catch (IOException e) {
            e.printStackTrace();
            issueCommentSuccessful = false;
        }

        if (!issueCommentSuccessful) {
            throw new GradleException("Error sending violation reports: " + violationComment.getCommentJson());
        } else if (violationComment.isFailBuild() && failBuildOnError) {
            throw new GradleException("One or more comment reporters has forced the build to fail");
        }
    }

    private boolean sendViolationReport(String authToken, String repoName, String issueNumber, String commentBody) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.github.com/repos/" + repoName + "/issues/" + issueNumber + "/comments").openConnection();
        httpURLConnection.setRequestProperty("Authorization", "token " + authToken);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);

        //println "Sending violation reports";

        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
        dataOutputStream.writeBytes(commentBody);
        dataOutputStream.flush();
        dataOutputStream.close();

        int statusCode = httpURLConnection.getResponseCode();
        return statusCode >= 200 && statusCode < 300;
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
