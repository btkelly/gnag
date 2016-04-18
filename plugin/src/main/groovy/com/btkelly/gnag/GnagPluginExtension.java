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

import com.btkelly.gnag.extensions.GitHubExtension;
import com.btkelly.gnag.extensions.ReporterExtension;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagPluginExtension {

    private static final String EXTENSION_NAME = "gnag";

    public static GnagPluginExtension loadExtension(@NotNull Project project) {
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

    public GitHubExtension github;

    public void github(Action<GitHubExtension> action) {
        action.execute(github);
    }

    private boolean enabled = true;
    private boolean failOnError = true;

    public GnagPluginExtension(@NotNull Project project) {
        this.project = project;
        this.github = new GitHubExtension(project);
        this.checkstyle = new ReporterExtension("CheckStyle", project);
        this.pmd = new ReporterExtension("PMD", project);
        this.findbugs = new ReporterExtension("FindBugs", project);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public boolean shouldFailOnError() {
        return project.hasProperty("failOnError") ? (Boolean) project.property("failOnError") : failOnError;
    }

    @Override
    public String toString() {
        return "GnagExtension{" +
                "project=" + project +
                ", checkstyle=" + checkstyle +
                ", pmd=" + pmd +
                ", github=" + github +
                ", findbugs=" + findbugs +
                ", enabled=" + enabled +
                ", failOnError=" + failOnError +
                '}';
    }
}
