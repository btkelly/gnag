/**
 * Copyright 2015 Bryan Kelly
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

import com.btkelly.gnag.GnagPluginExtension;
import com.btkelly.gnag.models.github.GitHubComment;
import com.btkelly.gnag.models.github.GitHubPullRequest;
import com.btkelly.gnag.models.github.GitHubStatus;
import com.btkelly.gnag.models.github.GitHubStatusType;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sun.javafx.beans.annotations.NonNull;
import org.gradle.api.Nullable;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class GitHubApi {

    public enum Status {
        OK,
        FAIL
    }

    private static GitHubApi defaultApi;

    public static GitHubApi defaultApi(GnagPluginExtension gnagPluginExtension) {
        synchronized (GitHubApi.class) {
            if (defaultApi == null) {
                defaultApi = new GitHubApi(gnagPluginExtension);
            }
        }

        return defaultApi;
    }

    private final GitHubApiClient gitHubApiClient;
    private final GnagPluginExtension gnagPluginExtension;

    private GitHubApi(final GnagPluginExtension gnagPluginExtension) {
        this.gnagPluginExtension = gnagPluginExtension;

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "token " + gnagPluginExtension.getGitHubAuthToken())
                        .build();

                return chain.proceed(request);
            }
        });

        String baseUrl = "https://api.github.com/repos/" + gnagPluginExtension.getGitHubRepoName() + "/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubApiClient = retrofit.create(GitHubApiClient.class);
    }

    @NonNull
    public Status postGitHubComment(String comment) {

        try {
            retrofit.Response<GitHubComment> gitHubCommentResponse = gitHubApiClient.postComment(new GitHubComment(comment), gnagPluginExtension.getGitHubIssueNumber()).execute();
            return gitHubCommentResponse.isSuccess() ? Status.OK : Status.FAIL;
        } catch (IOException ignored) {
            return Status.FAIL;
        }
    }

    @NonNull
    public Status postUpdatedGitHubStatus(GitHubStatusType gitHubStatusType) {

        try {
            retrofit.Response<GitHubStatus> gitHubCommentResponse = gitHubApiClient.postUpdatedStatus(new GitHubStatus(gitHubStatusType), gnagPluginExtension.getGitHubIssueNumber()).execute();
            return gitHubCommentResponse.isSuccess() ? Status.OK : Status.FAIL;
        } catch (IOException ignored) {
            return Status.FAIL;
        }
    }

    @Nullable
    public GitHubPullRequest getPullRequestDetails() {
        try {
            retrofit.Response<GitHubPullRequest> gitHubPullRequestResponse = gitHubApiClient.getPullRequest(gnagPluginExtension.getGitHubIssueNumber()).execute();
            return gitHubPullRequestResponse.body();
        } catch (IOException ignored) {
            return null;
        }
    }

}
