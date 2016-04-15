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

import com.btkelly.gnag.extensions.ReporterExtension;
import org.gradle.api.Action;
import org.gradle.api.Project;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagPluginExtension {

    private static final String EXTENSION_NAME = "gnag";

    public static GnagPluginExtension loadExtension(Project project) {
        return project.getExtensions().create(EXTENSION_NAME, GnagPluginExtension.class, project);
    }

    private final Project project;

    public ReporterExtension checkstyle;

    public void checkstyle(Action<ReporterExtension> action) {
        action.execute(checkstyle);
    }

    public ReporterExtension pmd;

    public void pmd(Action<ReporterExtension> action) {
        action.execute(pmd);
    }

    public ReporterExtension findbugs;

    public void findbugs(Action<ReporterExtension> action) {
        action.execute(findbugs);
    }

    private boolean skip;
    private boolean failOnError;
    private String gitHubRepoName;
    private String gitHubAuthToken;
    private String gitHubIssueNumber;

    public GnagPluginExtension(Project project) {
        this.project = project;
        checkstyle = new ReporterExtension("CheckStyle", project);
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

    @Override
    public String toString() {
        return "GnagExtension{" +
                "project=" + project +
                ", checkstyle=" + checkstyle +
                ", pmd=" + pmd +
                ", findbugs=" + findbugs +
                ", skip=" + skip +
                ", failOnError=" + failOnError +
                ", gitHubRepoName='" + gitHubRepoName + '\'' +
                ", gitHubAuthToken='" + gitHubAuthToken + '\'' +
                ", gitHubIssueNumber='" + gitHubIssueNumber + '\'' +
                '}';
    }
}
