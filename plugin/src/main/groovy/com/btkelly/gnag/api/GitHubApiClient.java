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

import com.btkelly.gnag.models.*;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by bobbake4 on 12/1/15.
 */
public interface GitHubApiClient {

    // https://developer.github.com/v3/issues/comments/#create-a-comment
    @POST("issues/{issueNumber}/comments")
    @Headers("Accept: application/vnd.github.v3+json")
    Call<GitHubIssueComment> postComment(@Body GitHubIssueComment gitHubIssueComment, @Path("issueNumber") String issueNumber);

    // https://developer.github.com/v3/repos/statuses/#create-a-status
    @POST("statuses/{sha}")
    @Headers("Accept: application/vnd.github.v3+json")
    Call<GitHubStatus> postUpdatedStatus(@Body GitHubStatus gitHubStatus, @Path("sha") String sha);

    // https://developer.github.com/v3/pulls/#get-a-single-pull-request
    @GET("pulls/{issueNumber}")
    @Headers("Accept: application/vnd.github.v3+json")
    Call<GitHubPRDetails> getPRDetails(@Path("issueNumber") String issueNumber);

    // https://developer.github.com/v3/pulls/#get-a-single-pull-request
    // https://developer.github.com/v3/media/#commits-commit-comparison-and-pull-requests
    @GET("pulls/{issueNumber}")
    @Headers("Accept: application/vnd.github.v3.diff")
    Call<GitHubPRDiffWrapper> getPRDiffWrapper(@Path("issueNumber") String issueNumber);

    // https://developer.github.com/v3/pulls/comments/#create-a-comment
    @POST("pulls/{issueNumber}/comments")
    @Headers("Accept: application/vnd.github.v3+json")
    Call<Void> postComment(@Body GitHubPRComment gitHubPRComment, @Path("issueNumber") String issueNumber);
    
}
