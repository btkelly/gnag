package com.btkelly.gnag.models;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public final class GitHubPRComment {

    @NotNull
    @SerializedName("body")
    private final String body;
    
    @NotNull
    @SerializedName("commit_id")
    private final String prSha;
    
    @NotNull
    @SerializedName("path")
    private final String relativeFilePath;

    @SerializedName("position")
    private final int diffLineIndex;

    public GitHubPRComment(
            @NotNull final String body,
            @NotNull final String prSha,
            @NotNull final String relativeFilePath,
            final int diffLineIndex) {
        
        this.body = body;
        this.prSha = prSha;
        this.relativeFilePath = relativeFilePath;
        this.diffLineIndex = diffLineIndex;
    }

    @Override
    public String toString() {
        return "GitHubIssueComment{" +
                "body='" + body + '\'' +
                "prSha='" + prSha + '\'' +
                "relativeFilePath='" + relativeFilePath + '\'' +
                "diffLineIndex='" + diffLineIndex + '\'' +
                '}';
    }
    
}
