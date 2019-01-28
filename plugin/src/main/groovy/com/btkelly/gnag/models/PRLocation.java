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

public final class PRLocation {

    @NotNull
    private final String relativeFilePath;

    private final int diffLineIndex;

    public PRLocation(@NotNull final String relativeFilePath, final int diffLineIndex) {
        this.relativeFilePath = relativeFilePath;
        this.diffLineIndex = diffLineIndex;
    }

    @NotNull
    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    public int getDiffLineIndex() {
        return diffLineIndex;
    }

    @Override public String toString() {
        return "PRLocation{" +
               "relativeFilePath='" + relativeFilePath + '\'' +
               ", diffLineIndex=" + diffLineIndex +
               '}';
    }
}
