package com.btkelly.gnag

/**
 * Created by bobbake4 on 10/29/15.
 */
class GnagPluginConvention {

    String gitHubRepoUrl;
    String gitHubAuthToken;
    String gitHubIssueNumber;
    boolean failBuildOnError;

    def gnag(Closure closure) {
        closure.delegate = this;
        closure();
    }

    boolean hasValidConfig() {
        return gitHubAuthToken != null && gitHubRepoUrl != null && gitHubIssueNumber != null;
    }

}
