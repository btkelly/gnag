package com.btkelly.gnag.models;

import com.google.gson.Gson;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class ViolationComment {

    private final boolean failBuild;
    private final String commentMessage;

    public ViolationComment(boolean failBuild, String commentMessage) {
        this.failBuild = failBuild;
        this.commentMessage = commentMessage;
    }

    public boolean isFailBuild() {
        return failBuild;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public String getCommentJson() {
        GitHubComment gitHubComment = new GitHubComment(getCommentMessage());
        return new Gson().toJson(gitHubComment);
    }

    public class GitHubComment {

        private final String body;

        public GitHubComment(String body) {
            this.body = body;
        }

    }
}
