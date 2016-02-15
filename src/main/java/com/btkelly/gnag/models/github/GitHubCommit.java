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
public class GitHubCommit {

    private String label;
    private String ref;
    private String sha;

    public String getLabel() {
        return label;
    }

    public String getRef() {
        return ref;
    }

    public String getSha() {
        return sha;
    }

    @Override
    public String toString() {
        return "GitHubCommit{" +
                "label='" + label + '\'' +
                ", ref='" + ref + '\'' +
                ", sha='" + sha + '\'' +
                '}';
    }
}
