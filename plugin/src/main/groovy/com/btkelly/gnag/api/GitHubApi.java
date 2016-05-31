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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class GitHubApi {
    
    private final GitHubApiClient gitHubApiClient;
    private final GitHubExtension gitHubExtension;

    public GitHubApi(final GitHubExtension gitHubExtension) {
        this.gitHubExtension = gitHubExtension;

        HttpLoggingInterceptor.Logger logger = System.out::println;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new UserAgentInterceptor())
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

    public void postGitHubPRCommentAsync(final String comment) {
        gitHubApiClient.postPRComment(new GitHubPRComment(comment), gitHubExtension.getIssueNumber())
                .enqueue(new DefaultCallback<>());
    }

    public void postUpdatedGitHubStatusAsync(final GitHubStatusType gitHubStatusType, final String prSha) {
        gitHubApiClient.postUpdatedStatus(new GitHubStatus(gitHubStatusType), prSha)
                .enqueue(new DefaultCallback<>());
    }

    @Nullable
    public GitHubPRDetails getPRDetailsSync() {
        try {
            Response<GitHubPRDetails> gitHubPRDetailsResponse = gitHubApiClient.getPRDetails(gitHubExtension.getIssueNumber()).execute();
            return gitHubPRDetailsResponse.body();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public List<Diff> getPRDiffsSync() {
        try {
            final Response<List<Diff>> gitHubPRDiffsResponse
                    = gitHubApiClient.getPRDiffs(gitHubExtension.getIssueNumber()).execute();
            
            return gitHubPRDiffsResponse.body();
        } catch (final Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void postGitHubInlineCommentSync(
            @NotNull final String body,
            @NotNull final String prSha,
            @NotNull final PRLocation prLocation) {

        try {
            gitHubApiClient.postInlineComment(
                    new GitHubInlineComment(body, prSha, prLocation), gitHubExtension.getIssueNumber())
                            .execute();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private static final class DefaultCallback<T> implements Callback<T> {

        private DefaultCallback() {
            // This constructor intentionally left blank.
        }

        @Override
        public void onResponse(final Call<T> call, final Response<T> response) {
            // This method intentionally left blank.            
        }

        @Override
        public void onFailure(final Call<T> call, final Throwable t) {
            t.printStackTrace();
        }
        
    }

}
