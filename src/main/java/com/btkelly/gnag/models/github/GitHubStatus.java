package com.btkelly.gnag.models.github;

/**
 * Created by bobbake4 on 12/2/15.
 */
public class GitHubStatus {

    private String state;
    private String description;
    private String context;

    public GitHubStatus(GitHubStatusType state) {
        this.state = state.toString();
        this.description = state.getDescription();
        this.context = "continuous-integration/gnag";
    }
}
