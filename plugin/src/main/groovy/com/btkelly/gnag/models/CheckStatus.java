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

/**
 * Created by bobbake4 on 4/26/16.
 */
public class CheckStatus {

    private final String comment;
    private final GitHubStatusType gitHubStatusType;

    public CheckStatus(String comment, GitHubStatusType gitHubStatusType) {
        this.comment = comment;
        this.gitHubStatusType = gitHubStatusType;
    }

    public String getComment() {
        return comment;
    }

    public GitHubStatusType getGitHubStatusType() {
        return gitHubStatusType;
    }

    @Override
    public String toString() {
        return gitHubStatusType.toString();
    }
}
