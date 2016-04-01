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
package com.btkelly.gnag;

import com.btkelly.gnag.extentions.ReporterExtension;
import org.gradle.api.Project;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagExtension {

    public static final String NAME = "gnag";

    private final Project project;
    private final ReporterExtension checkStyle;
    private final ReporterExtension pmd;
    private final ReporterExtension findbugs;

    private boolean skip;
    private boolean failOnError;
    private String gitHubRepoName;
    private String gitHubAuthToken;
    private String gitHubIssueNumber;

    public GnagExtension(Project project) {
        this.project = project;
        checkStyle = new ReporterExtension("CheckStyle", project);
        pmd = new ReporterExtension("PMD", project);
        findbugs = new ReporterExtension("FindBugs", project);
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean shouldSkip() {
        return skip;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public boolean shouldFailOnError() {
        return project.hasProperty("failOnError") ? (Boolean) project.property("failOnError") : failOnError;
    }

    public void setGitHubRepoName(String gitHubRepoName) {
        this.gitHubRepoName = gitHubRepoName;
    }

    public String getGitHubRepoName() {
        return project.hasProperty("gitHubRepoName") ? (String) project.property("gitHubRepoName") : gitHubRepoName;
    }

    public void setGitHubAuthToken(String gitHubAuthToken) {
        this.gitHubAuthToken = gitHubAuthToken;
    }

    public String getGitHubAuthToken() {
        return project.hasProperty("gitHubAuthToken") ? (String) project.property("gitHubAuthToken") : gitHubAuthToken;
    }

    public void setGitHubIssueNumber(String gitHubIssueNumber) {
        this.gitHubIssueNumber = gitHubIssueNumber;
    }

    public String getGitHubIssueNumber() {
        return project.hasProperty("gitHubIssueNumber") ? (String) project.property("gitHubIssueNumber") : gitHubIssueNumber;
    }

}
