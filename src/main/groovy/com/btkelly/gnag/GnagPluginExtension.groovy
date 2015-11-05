package com.btkelly.gnag

import org.gradle.api.Project

/**
 * Gradle extension to allow configuration of plugin from Gradle file
 */
class GnagPluginExtension {

    /**
     * Creates the extension "gnag" on the current project
     * @param project
     */
    public static void loadExtension(Project project) {
        project.extensions.create("gnag", GnagPluginExtension);
    }

    /**
     * Name of the GitHub repo to post comments to, ex: "btkelly/gnag"
     */
    def String gitHubRepoName;

    /**
     * GitHub authentication token of a user to post comments as
     */
    def String gitHubAuthToken;

    /**
     * The issue number of the current build to post comments to
     */
    def String gitHubIssueNumber;

    /**
     * Flag to indicate if the Gnag plugin should make the build as a failure if one of the reporters indicates to do so
     */
    def boolean failBuildOnError;

    /**
     * Checks to make sure the current project has the required parameters (gitHubRepoName, gitHubAuthToken, gitHubIssueNumber)
     * @param project
     * @return
     */
    boolean hasValidConfig(Project project) {
        return getGitHubAuthToken(project) != null && getGitHubRepoName(project) != null && getGitHubIssueNumber(project) != null;
    }

    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @param project
     * @return
     */
    String getGitHubRepoName(Project project) {
        return project.hasProperty("gitHubRepoName") ? project.property("gitHubRepoName") : gitHubRepoName;
    }


    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @param project
     * @return
     */
    String getGitHubAuthToken(Project project) {
        return project.hasProperty("gitHubAuthToken") ? project.property("gitHubAuthToken") : gitHubAuthToken;
    }


    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @param project
     * @return
     */
    String getGitHubIssueNumber(Project project) {
        return project.hasProperty("gitHubIssueNumber") ? project.property("gitHubIssueNumber") : gitHubIssueNumber;
    }


    /**
     * If a command line parameter was provided let that take precedence otherwise return gradle config value
     * @param project
     * @return
     */
    boolean getFailBuildOnError(Project project) {
        return project.hasProperty("failBuildOnError") ? project.property("failBuildOnError") : failBuildOnError;
    }
}
