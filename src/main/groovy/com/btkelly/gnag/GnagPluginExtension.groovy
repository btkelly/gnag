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

    boolean hasValidConfig() {
        return gitHubAuthToken != null && gitHubRepoName != null && gitHubIssueNumber != null;
    }

}
