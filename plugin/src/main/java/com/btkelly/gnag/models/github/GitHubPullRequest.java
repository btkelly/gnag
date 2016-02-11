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
package com.btkelly.gnag.models.github;

/**
 * Created by bobbake4 on 12/2/15.
 */
public class GitHubPullRequest {

    private int id;
    private int number;
    private String state;
    private String title;
    private String body;
    private String statusesUrl;
    private GitHubCommit head;
    private GitHubCommit base;

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getStatusesUrl() {
        return statusesUrl;
    }

    public GitHubCommit getHead() {
        return head;
    }

    public GitHubCommit getBase() {
        return base;
    }

    @Override
    public String toString() {
        return "GitHubPullRequest{" +
                "id=" + id +
                ", number=" + number +
                ", state='" + state + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", statusesUrl='" + statusesUrl + '\'' +
                ", head=" + head +
                ", base=" + base +
                '}';
    }
}
