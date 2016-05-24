package com.btkelly.gnag.models;

import org.jetbrains.annotations.NotNull;
import org.wickedsource.diffparser.api.model.Diff;

import java.util.List;

public final class GitHubPullRequestDiffWrapper {

    @NotNull
    private final String rawDiff;

    @NotNull
    private final List<Diff> parsedDiffs;

    /*
     * From GitHub documentation (https://developer.github.com/v3/pulls/comments/#create-a-comment):
     * 
     *     To comment on a specific line in a file, you will need to first determine the position in the diff. [...]
     *     The position value is the number of lines down from the first "@@" hunk header in the file you would like to
     *     comment on. The line just below the "@@" line is position 1, the next line is position 2, and so on.
     *     The position in the file's diff continues to increase through lines of whitespace and additional hunks until
     *     a new file is reached.
     *     
     * 
     * 
     */

    public GitHubPullRequestDiffWrapper(
            @NotNull final String rawDiff,
            @NotNull final List<Diff> parsedDiffs) {

        if (parsedDiffs.isEmpty()) {
            throw new IllegalStateException("Must supply at least one diff.");
        }

        this.rawDiff = rawDiff;
        this.parsedDiffs = parsedDiffs;
    }
    
}
