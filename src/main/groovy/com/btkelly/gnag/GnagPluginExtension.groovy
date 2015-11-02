package com.btkelly.gnag

import org.gradle.api.Project

/**
 * Created by bobbake4 on 10/29/15.
 */
class GnagPluginExtension {

    public static void loadExtension(Project project) {
        project.extensions.create("gnag", GnagPluginExtension);
    }

    def String gitHubRepoName;
    def String gitHubAuthToken;
    def String gitHubIssueNumber;
    def boolean failBuildOnError;

    boolean hasValidConfig(Project project) {
        return getGitHubAuthToken(project) != null && getGitHubRepoName(project) != null && getGitHubIssueNumber(project) != null;
    }

    String getGitHubRepoName(Project project) {
        return project.hasProperty("gitHubRepoName") ? project.property("gitHubRepoName") : gitHubRepoName;
    }

    String getGitHubAuthToken(Project project) {
        return project.hasProperty("gitHubAuthToken") ? project.property("gitHubAuthToken") : gitHubAuthToken;
    }

    String getGitHubIssueNumber(Project project) {
        return project.hasProperty("gitHubIssueNumber") ? project.property("gitHubIssueNumber") : gitHubIssueNumber;
    }

    boolean getFailBuildOnError(Project project) {
        return project.hasProperty("failBuildOnError") ? project.property("failBuildOnError") : failBuildOnError;
    }
}
