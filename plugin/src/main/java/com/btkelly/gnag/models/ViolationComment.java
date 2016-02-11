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

import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class ViolationComment {

    private final boolean failBuild;
    private final String commentMessage;

    public ViolationComment(boolean failBuild, @NotNull String commentMessage) {
        this.failBuild = failBuild;
        this.commentMessage = commentMessage.trim().length() == 0 ? "Congrats! No :poop: code found, this PR is safe to merge." : commentMessage;
    }

    public boolean isFailBuild() {
        return failBuild;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

}
