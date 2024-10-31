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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class GitHubApi {

  private final GitHubApiClient gitHubApiClient;
  private final GitHubExtension gitHubExtension;
  private final Logger logger;

  public GitHubApi(final GitHubExtension gitHubExtension, Logger logger) {
    this.gitHubExtension = gitHubExtension;
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

  public void postGitHubPRComment(final String comment) {
    logger.debug("postGitHubPRComment: " + comment);
    try {
      gitHubApiClient.postPRComment(new GitHubPRComment(comment), gitHubExtension.getIssueNumber())
          .execute();
    } catch (IOException e) {
      logger.debug("postGitHubPRComment - Error: ", e);
    }
  }

  public void postUpdatedGitHubStatus(
      final GitHubStatusType gitHubStatusType,
      final String moduleName,
      final String prSha) {
    try {
      gitHubApiClient.postUpdatedStatus(new GitHubStatus(gitHubStatusType, moduleName), prSha)
          .execute();
    } catch (IOException e) {
      logger.debug("postUpdatedGitHubStatus - Error: ", e);
    }
  }

  @NotNull
  public GitHubPRDetails getPRDetails() {
    try {
      Response<GitHubPRDetails> gitHubPRDetailsResponse = gitHubApiClient
          .getPRDetails(gitHubExtension.getIssueNumber()).execute();

      if (!gitHubPRDetailsResponse.isSuccessful()
          || gitHubPRDetailsResponse.body().getHead() == null) {
        throw new GradleException("Failed to fetch PR details. Reason: "
            + gitHubPRDetailsResponse.code() + " " + gitHubPRDetailsResponse.message());
      }

      return gitHubPRDetailsResponse.body();
    } catch (IOException e) {
      throw new GradleException("Failed to fetch PR details.", e);
    }
  }

  @NotNull
  public List<Diff> getPRDiffs() {
    try {
      final Response<List<Diff>> gitHubPRDiffsResponse = gitHubApiClient
          .getPRDiffs(gitHubExtension.getIssueNumber()).execute();
      logger.debug("getPRDiffs - isSuccessful: " + gitHubPRDiffsResponse.isSuccessful());
      return gitHubPRDiffsResponse.body();
    } catch (final Exception e) {
      logger.debug("getPRDiffs - Error", e);
      return new ArrayList<>();
    }
  }

  public void postGitHubInlineComment(
      @NotNull final String body,
      @NotNull final String prSha,
      @NotNull final PRLocation prLocation) {
    try {
      gitHubApiClient.postInlineComment(
          new GitHubInlineComment(body, prSha, prLocation), gitHubExtension.getIssueNumber())
          .execute();
    } catch (final Exception e) {
      logger.debug("postGitHubInlineComment - Error", e);
    }
  }

}