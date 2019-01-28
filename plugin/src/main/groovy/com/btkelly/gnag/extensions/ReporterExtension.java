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

import java.io.File;
import org.gradle.api.Project;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class ReporterExtension {

  private final String name;
  private final Project project;

  private boolean enabled = true;
  private File reporterConfig;
  private String toolVersion;

  public ReporterExtension(String name, Project project) {
    this.name = name;
    this.project = project;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * @deprecated replaced by {@link #setEnabled(boolean)}
   */
  @Deprecated
  public void enabled(boolean enabled) {
    this.enabled = enabled;
  }

  public File getReporterConfig() {
    return reporterConfig;
  }

  public void reporterConfig(File reporterConfig) {
    this.reporterConfig = reporterConfig;
  }

  public boolean hasReporterConfig() {
    return reporterConfig != null;
  }

  public String getToolVersion() {
    return toolVersion;
  }

  public void toolVersion(String toolVersion) {
    this.toolVersion = toolVersion;
  }

  @Override
  public String toString() {
    return "ReporterExtension{" +
           "name='" + name + '\'' +
           ", project=" + project +
           ", enabled=" + enabled +
           ", reporterConfig=" + reporterConfig +
           ", toolVersion='" + toolVersion + '\'' +
           '}';
  }
}
