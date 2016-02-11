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
public enum GitHubStatusType {

    SUCCESS("All code quality checks have passed"),
    PENDING("Checking code quality"),
    ERROR("An error was encountered while checking code quality"),
    FAILURE("Violations found, check PR comments for details");

    private final String description;

    GitHubStatusType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
