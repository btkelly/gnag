package com.btkelly.gnag;

import org.gradle.api.Project;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class GnagPluginExtension {

    /**
     * Creates the extension "gnag" on the current project
     * @param project
     */
    public static void loadExtension(Project project) {
        project.getExtensions().create("gnag", GnagPluginExtension.class, project);
    }

    private final Project project;

    public GnagPluginExtension(Project project) {
        this.project = project;
    }

    /**
     * Name of the GitHub repo to post comments to, ex: "btkelly/gnag"
     */
    private String gitHubRepoName;

    /**
     * GitHub authentication token of a user to post comments as
     */
    private String gitHubAuthToken;

    /**
     * The issue number of the current build to post comments to
     */
    private String gitHubIssueNumber;

    /**
     * Flag to indicate if the Gnag plugin should make the build as a failure if one of the reporters indicates to do so
     */
    private boolean failBuildOnError;

    /**
     * Checks to make sure the current project has the required parameters (gitHubRepoName, gitHubAuthToken, gitHubIssueNumber)
     * @return - true if the project has a valid config false if it does not
     */
    public boolean hasValidConfig() {
        return getGitHubAuthToken() != null && getGitHubRepoName() != null && getGitHubIssueNumber() != null;
    }

    /**
     * Allows setting the repo name from the Gradle file
     * @param gitHubRepoName
     */
    public void setGitHubRepoName(String gitHubRepoName) {
        this.gitHubRepoName = gitHubRepoName;
    }

    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @return - GitHub Repo Name set by command line or the value set in gradle file if no command line variable present
     */
    public String getGitHubRepoName() {
        return project.hasProperty("gitHubRepoName") ? (String) project.property("gitHubRepoName") : gitHubRepoName;
    }

    /**
     * Allows setting the auth token from the Gradle file
     * @param gitHubAuthToken
     */
    public void setGitHubAuthToken(String gitHubAuthToken) {
        this.gitHubAuthToken = gitHubAuthToken;
    }

    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @return - GitHub Auth Token set by command line or the value set in gradle file if no command line variable present
     */
    public String getGitHubAuthToken() {
        return project.hasProperty("gitHubAuthToken") ? (String) project.property("gitHubAuthToken") : gitHubAuthToken;
    }

    /**
     * Allows setting the issue number from the Gradle file
     * @param gitHubIssueNumber
     */
    public void setGitHubIssueNumber(String gitHubIssueNumber) {
        this.gitHubIssueNumber = gitHubIssueNumber;
    }

    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @return - GitHub Issue Number set by command line or the value set in gradle file if no command line variable present
     */
    public String getGitHubIssueNumber() {
        return project.hasProperty("gitHubIssueNumber") ? (String) project.property("gitHubIssueNumber") : gitHubIssueNumber;
    }

    /**
     * Allows setting the fail build on error from the Gradle file
     * @param failBuildOnError
     */
    public void setFailBuildOnError(boolean failBuildOnError) {
        this.failBuildOnError = failBuildOnError;
    }

    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @return - Fail Build On Error set by command line or the value set in gradle file if no command line variable present
     */
    public boolean getFailBuildOnError() {
        return project.hasProperty("failBuildOnError") ? (Boolean) project.property("failBuildOnError") : failBuildOnError;
    }
}
