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
import com.btkelly.gnag.models.*;
import com.btkelly.gnag.utils.diffparser.DiffParserConverterFactory;
import com.btkelly.gnag.utils.gson.GsonConverterFactory;
import com.github.stkent.githubdiffparser.models.Diff;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class GitHubApi {

    private final GitHubApiClient gitHubApiClient;
    private final GitHubExtension gitHubExtension;
    private final Executor singleThreadExecutor;
    private final Logger logger;

    public GitHubApi(final GitHubExtension gitHubExtension, Logger logger) {
        this.gitHubExtension = gitHubExtension;
        this.singleThreadExecutor = Executors.newSingleThreadExecutor();
        this.logger = logger;

        HttpLoggingInterceptor.Logger loggingInterceptor = logger::debug;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor())
                .addInterceptor(new AuthInterceptor(gitHubExtension.getAuthToken()))
                .addInterceptor(new HttpLoggingInterceptor(loggingInterceptor).setLevel(Level.BODY))
                .build();

        String baseUrl = gitHubExtension.getRootUrl() + gitHubExtension.getRepoName() + "/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(DiffParserConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gitHubApiClient = retrofit.create(GitHubApiClient.class);
    }

    public void postGitHubPRCommentAsync(final String comment) {
        logger.debug("postGitHubPRCommentAsync: " + comment);
        gitHubApiClient.postPRComment(new GitHubPRComment(comment), gitHubExtension.getIssueNumber())
                .enqueue(new DefaultCallback<>());
    }

    public void postUpdatedGitHubStatusAsync(
            final GitHubStatusType gitHubStatusType,
            final String moduleName,
            final String prSha) {
        logger.debug("postUpdatedGitHubStatusAsync - Status: " + gitHubStatusType);
        logger.debug("postUpdatedGitHubStatusAsync - Module Name: " + gitHubStatusType);
        logger.debug("postUpdatedGitHubStatusAsync - PR Sha: " + gitHubStatusType);

        singleThreadExecutor.execute(() -> {

            boolean isSuccessful = false;
            int retryCount = 0;

            while (!isSuccessful && retryCount < 5) {
                try {
                    isSuccessful = gitHubApiClient.postUpdatedStatus(new GitHubStatus(gitHubStatusType, moduleName), prSha)
                            .execute()
                            .isSuccessful();
                    logger.debug("postUpdatedGitHubStatusAsync - isSuccessful: " + isSuccessful + ", retry count: " + retryCount);
                } catch (IOException e) {
                    logger.debug("postUpdatedGitHubStatusAsync - Error retry count: " + retryCount, e);
                    e.printStackTrace();
                }

                retryCount++;
            }
        });
    }

    @NotNull
    public GitHubPRDetails getPRDetailsSync() {
        try {
            Response<GitHubPRDetails> gitHubPRDetailsResponse = gitHubApiClient.getPRDetails(gitHubExtension.getIssueNumber()).execute();

            if (!gitHubPRDetailsResponse.isSuccessful()) {
                throw new GradleException("Failed to fetch PR details. Reason: "
                        + gitHubPRDetailsResponse.code() + " " + gitHubPRDetailsResponse.message());
            }

            logger.debug("getPRDetailsSync - response: " + gitHubPRDetailsResponse.body());

            return gitHubPRDetailsResponse.body();
        } catch (IOException e) {
            throw new GradleException("Failed to fetch PR details.", e);
        }
    }

    @NotNull
    public List<Diff> getPRDiffsSync() {
        try {
            final Response<List<Diff>> gitHubPRDiffsResponse
                    = gitHubApiClient.getPRDiffs(gitHubExtension.getIssueNumber()).execute();

            logger.debug("getPRDiffsSync - isSuccessful: " + gitHubPRDiffsResponse.isSuccessful());

            return gitHubPRDiffsResponse.body();
        } catch (final Exception e) {
            logger.debug("getPRDiffsSync - Error", e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void postGitHubInlineCommentSync(
            @NotNull final String body,
            @NotNull final String prSha,
            @NotNull final PRLocation prLocation) {
        logger.debug("postGitHubInlineCommentSync - Body: " + body);
        logger.debug("postGitHubInlineCommentSync - PR Sha: " + prSha);
        logger.debug("postGitHubInlineCommentSync - PR Location: " + prLocation);

        try {
            gitHubApiClient.postInlineComment(
                    new GitHubInlineComment(body, prSha, prLocation), gitHubExtension.getIssueNumber())
                    .execute();
        } catch (final Exception e) {
            logger.debug("postGitHubInlineCommentSync - Error", e);
            e.printStackTrace();
        }
    }

    private final class DefaultCallback<T> implements Callback<T> {

        private DefaultCallback() {
            // This constructor intentionally left blank.
        }

        @Override
        public void onResponse(final Call<T> call, final Response<T> response) {
            logger.debug(call.toString() + " - onResponse" + response);
        }

        @Override
        public void onFailure(final Call<T> call, final Throwable t) {
            logger.debug(call.toString() + " - onFailure", t);
            t.printStackTrace();
        }

    }

}