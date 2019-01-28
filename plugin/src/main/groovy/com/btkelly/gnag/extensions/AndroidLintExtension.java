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
 * Created by bobbake4 on 4/19/16.
 */
public class AndroidLintExtension {

  public static final String SEVERITY_WARNING = "Warning";
  public static final String SEVERITY_ERROR = "Error";

  private final Project project;

  private boolean enabled = true;
  private String severity = SEVERITY_ERROR;

  public AndroidLintExtension(Project project) {
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

  public String getSeverity() {
    return severity;
  }

  public void severity(String severity) {
    this.severity = severity;
  }

  @Override
  public String toString() {
    return "AndroidLintExtension{" +
           "project=" + project +
           ", enabled=" + enabled +
           ", severity='" + severity + '\'' +
           '}';
  }
}
