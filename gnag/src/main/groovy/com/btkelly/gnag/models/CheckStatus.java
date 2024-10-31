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
package com.btkelly.gnag.models;

import static com.btkelly.gnag.models.GitHubStatusType.SUCCESS;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 4/26/16.
 */
public class CheckStatus {

  @NotNull
  private final GitHubStatusType gitHubStatusType;
  @NotNull
  private final Set<Violation> violations;

  public CheckStatus(@NotNull final GitHubStatusType gitHubStatusType,
      @NotNull final Set<Violation> violations) {
    if (gitHubStatusType == SUCCESS && !violations.isEmpty()) {
      throw new IllegalStateException("Status cannot be SUCCESS if violations are found.");
    }

    this.violations = violations;
    this.gitHubStatusType = gitHubStatusType;
  }

  public static CheckStatus getSuccessfulCheckStatus() {
    return new CheckStatus(SUCCESS, new HashSet<>());
  }

  @NotNull
  public GitHubStatusType getGitHubStatusType() {
    return gitHubStatusType;
  }

  @NotNull
  public Set<Violation> getViolations() {
    return violations;
  }

  @Override
  public String toString() {
    return gitHubStatusType.toString();
  }

}
