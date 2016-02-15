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

import com.btkelly.gnag.models.github.GitHubComment;
import com.btkelly.gnag.models.github.GitHubPullRequest;
import com.btkelly.gnag.models.github.GitHubStatus;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by bobbake4 on 12/1/15.
 */
public interface GitHubApiClient {

    @POST("issues/{issueNumber}/comments")
    Call<GitHubComment> postComment(@Body GitHubComment gitHubComment, @Path("issueNumber") String issueNumber);

    @POST("statuses/{sha}")
    Call<GitHubStatus> postUpdatedStatus(@Body GitHubStatus gitHubStatus, @Path("sha") String sha);

    @GET("pulls/{issueNumber}")
    Call<GitHubPullRequest> getPullRequest(@Path("issueNumber") String issueNumber);

}
