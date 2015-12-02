package com.btkelly.gnag.models.github;

/**
 * Created by bobbake4 on 12/2/15.
 */
public class GitHubPullRequest {

    private int id;
    private int number;
    private String state;
    private String title;
    private String body;
    private GitHubCommit head;
    private GitHubCommit base;

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public GitHubCommit getHead() {
        return head;
    }

    public GitHubCommit getBase() {
        return base;
    }
}
