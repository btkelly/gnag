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

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagPluginExtension {

  private static final String EXTENSION_NAME = "gnag";
  private final Project project;
  public ReporterExtension checkstyle;
  public ReporterExtension pmd;
  public ReporterExtension findbugs;
  public ReporterExtension ktlint;
  public ReporterExtension detekt;
  public GitHubExtension github;
  public AndroidLintExtension androidLint;
  private boolean enabled = true;
  private boolean failOnError = true;

  public GnagPluginExtension(@NotNull Project project) {
    this.project = project;
    this.github = new GitHubExtension(project);
    this.checkstyle = new ReporterExtension("CheckStyle", project);
    this.pmd = new ReporterExtension("PMD", project);
    this.findbugs = new ReporterExtension("FindBugs", project);
    this.ktlint = new ReporterExtension("ktlint", project);
    this.detekt = new ReporterExtension("detekt", project);
    this.androidLint = new AndroidLintExtension(project);
  }

  public static GnagPluginExtension loadExtension(@NotNull Project project) {
    return project.getExtensions().create(EXTENSION_NAME, GnagPluginExtension.class, project);
  }

  public void checkstyle(Action<ReporterExtension> action) {
    action.execute(checkstyle);
  }

  public void pmd(Action<ReporterExtension> action) {
    action.execute(pmd);
  }

  public void findbugs(Action<ReporterExtension> action) {
    action.execute(findbugs);
  }

  public void ktlint(Action<ReporterExtension> action) {
    action.execute(ktlint);
  }

  public void detekt(Action<ReporterExtension> action) {
    action.execute(detekt);
  }

  public void github(Action<GitHubExtension> action) {
    action.execute(github);
  }

  public void androidLint(Action<AndroidLintExtension> action) {
    action.execute(androidLint);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setFailOnError(boolean failOnError) {
    this.failOnError = failOnError;
  }

  public boolean shouldFailOnError() {
    return project.hasProperty("failOnError") ? (Boolean) project.property("failOnError") : failOnError;
  }

  @Override
  public String toString() {
    return "GnagPluginExtension{" +
           "project=" + project +
           ", checkstyle=" + checkstyle +
           ", pmd=" + pmd +
           ", findbugs=" + findbugs +
           ", ktlint=" + ktlint +
           ", detekt=" + detekt +
           ", github=" + github +
           ", androidLint=" + androidLint +
           ", enabled=" + enabled +
           ", failOnError=" + failOnError +
           '}';
  }
}
