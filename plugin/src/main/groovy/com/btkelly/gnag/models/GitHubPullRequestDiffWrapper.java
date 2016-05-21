package com.btkelly.gnag.models;

import org.jetbrains.annotations.NotNull;
import org.wickedsource.diffparser.api.model.Diff;

public class GitHubPullRequestDiffWrapper {

    @NotNull
    private final Diff diff;

    public GitHubPullRequestDiffWrapper(@NotNull final Diff diff) {
        this.diff = diff;
    }

}
