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
package com.btkelly.gnag.api;

import com.btkelly.gnag.extensions.GitHubExtension;
import com.btkelly.gnag.models.GitHubComment;
import com.btkelly.gnag.models.GitHubPullRequest;
import com.btkelly.gnag.models.GitHubStatus;
import com.btkelly.gnag.models.GitHubStatusType;
import com.btkelly.gnag.utils.diffparser.DiffParserConverterFactory;
import com.btkelly.gnag.utils.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class GitHubApi {

    public enum Status {
        OK,
        FAIL
    }

    private final GitHubApiClient gitHubApiClient;
    private final GitHubExtension gitHubExtension;

    public GitHubApi(final GitHubExtension gitHubExtension) {
        this.gitHubExtension = gitHubExtension;

        HttpLoggingInterceptor.Logger logger = System.out::println;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(gitHubExtension))
                .addInterceptor(new HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.NONE))
                .build();

        String baseUrl = "https://api.github.com/repos/" + gitHubExtension.getRepoName() + "/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(DiffParserConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubApiClient = retrofit.create(GitHubApiClient.class);
    }

    public Status postGitHubComment(String comment) {

        try {
            Response<GitHubComment> gitHubCommentResponse = gitHubApiClient.postComment(new GitHubComment(comment), gitHubExtension.getIssueNumber()).execute();
            return gitHubCommentResponse.isSuccessful() ? Status.OK : Status.FAIL;
        } catch (IOException ignored) {
            return Status.FAIL;
        }
    }

    public Status postUpdatedGitHubStatus(GitHubStatusType gitHubStatusType, String sha) {

        try {
            Response<GitHubStatus> gitHubStatusResponse = gitHubApiClient.postUpdatedStatus(new GitHubStatus(gitHubStatusType), sha).execute();
            return gitHubStatusResponse.isSuccessful() ? Status.OK : Status.FAIL;
        } catch (IOException ignored) {
            return Status.FAIL;
        }
    }

    public GitHubPullRequest getPullRequestDetails() {

        try {
            Response<GitHubPullRequest> gitHubPullRequestResponse = gitHubApiClient.getPullRequest(gitHubExtension.getIssueNumber()).execute();
            return gitHubPullRequestResponse.body();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

}
