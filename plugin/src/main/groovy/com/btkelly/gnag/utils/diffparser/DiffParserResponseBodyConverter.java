package com.btkelly.gnag.utils.diffparser;

import com.btkelly.gnag.models.GitHubPullRequestDiffWrapper;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.wickedsource.diffparser.api.DiffParser;
import org.wickedsource.diffparser.api.model.Diff;
import retrofit2.Converter;

import java.io.IOException;
import java.util.List;

public class DiffParserResponseBodyConverter implements Converter<ResponseBody, GitHubPullRequestDiffWrapper> {

    @NotNull
    private final DiffParser diffParser;

    protected DiffParserResponseBodyConverter(@NotNull final DiffParser diffParser) {
        this.diffParser = diffParser;
    }

    @Override
    public GitHubPullRequestDiffWrapper convert(final ResponseBody value) throws IOException {
        try {
            final List<Diff> parsedDiffs = diffParser.parse(value.byteStream());

            if (parsedDiffs == null || parsedDiffs.size() != 1) {
                // We expect to find a single diff; treat any other outcome as erroneous.
                return null;
            }

            return new GitHubPullRequestDiffWrapper(parsedDiffs.get(0));
        } catch (final IllegalStateException e) {
            return null;
        } finally {
            value.close();
        }
    }

}
