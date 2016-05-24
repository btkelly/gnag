/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
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

            if (parsedDiffs == null || parsedDiffs.isEmpty()) {
                // We expect to find at least one diff; treat any other outcome as erroneous.
                return null;
            }

            return new GitHubPullRequestDiffWrapper(value.string(), parsedDiffs);
        } catch (final IllegalStateException e) {
            return null;
        } finally {
            value.close();
        }
    }

}
