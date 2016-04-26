package com.btkelly.gnag.models;

/**
 * Created by bobbake4 on 4/26/16.
 */
public class CheckStatus {

    private final String comment;
    private final GitHubStatusType gitHubStatusType;

    public CheckStatus(String comment, GitHubStatusType gitHubStatusType) {
        this.comment = comment;
        this.gitHubStatusType = gitHubStatusType;
    }

    public String getComment() {
        return comment;
    }

    public GitHubStatusType getGitHubStatusType() {
        return gitHubStatusType;
    }

    @Override
    public String toString() {
        return gitHubStatusType.toString();
    }
}
