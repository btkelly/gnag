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
import com.btkelly.gnag.models.GitHubInlineComment;
import com.btkelly.gnag.models.GitHubPRComment;
import com.btkelly.gnag.models.GitHubPRDetails;
import com.btkelly.gnag.models.GitHubStatus;
import com.btkelly.gnag.models.GitHubStatusType;
import com.btkelly.gnag.models.PRLocation;
import com.btkelly.gnag.utils.diffparser.DiffParserConverterFactory;
import com.btkelly.gnag.utils.gson.GsonConverterFactory;
import com.github.stkent.githubdiffparser.models.Diff;

import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class GitHubApi {

    private final GitHubApiClient gitHubApiClient;
    private final GitHubExtension gitHubExtension;

    public GitHubApi(final GitHubExtension gitHubExtension) {
        this.gitHubExtension = gitHubExtension;

        org.slf4j.Logger gradleLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        HttpLoggingInterceptor.Logger logger = gradleLogger::info;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor())
                .addInterceptor(new AuthInterceptor(gitHubExtension.getAuthToken()))
                .addInterceptor(new HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BASIC))
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

    public void postGitHubPRCommentSync(final String comment) {
      try {
        Response<GitHubPRComment> response = gitHubApiClient.postPRComment(new GitHubPRComment(comment), gitHubExtension.getIssueNumber())
            .execute();

        if (!response.isSuccessful()) {
          throw new GradleException("Failed to post Github PR comment. Reason: " + response.code()
              + " " + response.message());
        }
      } catch (IOException e) {
        throw new GradleException("Failed to post Github PR comment.", e);
      }
    }

    public void postUpdatedGitHubStatusSync(
        final GitHubStatusType gitHubStatusType,
        final String moduleName,
        final String prSha) {

      GitHubStatus gitHubStatus = new GitHubStatus(gitHubStatusType, moduleName);

      try {
        Response<GitHubStatus> response = gitHubApiClient.postUpdatedStatus(gitHubStatus, prSha)
            .execute();

        if (!response.isSuccessful()) {
          throw new GradleException("Failed to update Github status for commit. Reason: " + response.code()
              + " " + response.message());
        }
      } catch (IOException e) {
        throw new GradleException("Failed to post Github PR comment.", e);
      }
    }

    @NotNull
    public GitHubPRDetails getPRDetailsSync() {
      try {
        Response<GitHubPRDetails> gitHubPRDetailsResponse = gitHubApiClient.getPRDetails(gitHubExtension.getIssueNumber()).execute();

        if (!gitHubPRDetailsResponse.isSuccessful()) {
          throw new GradleException("Failed to fetch PR details. Reason: "
              + gitHubPRDetailsResponse.code() + " " + gitHubPRDetailsResponse.message());
        }

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

        if (!gitHubPRDiffsResponse.isSuccessful()) {
          throw new GradleException("Failed to fetch PR diffs. Reason: "
              + gitHubPRDiffsResponse.code() + " " + gitHubPRDiffsResponse.message());
        }

        return gitHubPRDiffsResponse.body();
      } catch (Exception e) {
        throw new GradleException("Failed to fetch PR diffs.", e);
      }
    }

    public void postGitHubInlineCommentSync(
        @NotNull final String body,
        @NotNull final String prSha,
        @NotNull final PRLocation prLocation) {

      try {
        Response<Void> githubInlineCommentResponse = gitHubApiClient.postInlineComment(
            new GitHubInlineComment(body, prSha, prLocation), gitHubExtension.getIssueNumber())
            .execute();

        if (!githubInlineCommentResponse.isSuccessful()) {
          throw new GradleException("Failed to post Github inline comment. Reason: "
              + githubInlineCommentResponse.code() + " " + githubInlineCommentResponse.message());
        }
      } catch (final Exception e) {
        throw new GradleException("Failed to post Github inline comment.", e);
      }
    }
}
