package com.btkelly.gnag.models;

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
        return "{ \"body\" : \"" + commentMessage + "\" }";
    }
}
