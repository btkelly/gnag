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
package com.btkelly.gnag.models;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public final class GitHubInlineComment {

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

    public GitHubInlineComment(
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
        return "GitHubInlineComment{" +
                "body='" + body + '\'' +
                "prSha='" + prSha + '\'' +
                "relativeFilePath='" + relativeFilePath + '\'' +
                "diffLineIndex='" + diffLineIndex + '\'' +
                '}';
    }
    
}
