/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.btkelly.gnag.extensions;

import org.gradle.api.Project;

/**
 * Created by bobbake4 on 4/18/16.
 */
public final class GitHubExtension {

  private final Project project;

  private String rootUrl;
  private String repoName;
  private String authToken;
  private String issueNumber;
  private boolean commentInline = true;
  private boolean commentOnSuccess = true;

  GitHubExtension(Project project) {
    this.project = project;
  }

  public void rootUrl(String rootUrl) {
    this.rootUrl = rootUrl;
  }

  public String getRootUrl() {
    return project.hasProperty("rootUrl") ? (String) project.property("rootUrl") : rootUrl != null ? rootUrl : "https://api.github.com/repos/";
  }

  public void repoName(String repoName) {
    this.repoName = repoName;
  }

  public String getRepoName() {
    return project.hasProperty("repoName") ? (String) project.property("repoName") : repoName;
  }

  public void authToken(String authToken) {
    this.authToken = authToken;
  }

  public String getAuthToken() {
    return project.hasProperty("authToken") ? (String) project.property("authToken") : authToken;
  }

  public void issueNumber(String issueNumber) {
    this.issueNumber = issueNumber;
  }

  public String getIssueNumber() {
    return project.hasProperty("issueNumber") ? (String) project.property("issueNumber") : issueNumber;
  }

  public boolean isCommentInline() {
    return commentInline;
  }

  public void setCommentInline(final boolean commentInline) {
    this.commentInline = commentInline;
  }

  public boolean isCommentOnSuccess() {
    return commentOnSuccess;
  }

  public void setCommentOnSuccess(final boolean commentOnSuccess) {
    this.commentOnSuccess = commentOnSuccess;
  }

  @Override
  public String toString() {
    return "GitHubExtension{" +
           "project=" + project +
           ", rootUrl='" + rootUrl + '\'' +
           ", repoName='" + repoName + '\'' +
           ", authToken='" + authToken + '\'' +
           ", issueNumber='" + issueNumber + '\'' +
           ", commentInline=" + commentInline +
           ", commentOnSuccess=" + commentOnSuccess +
           '}';
  }
}
