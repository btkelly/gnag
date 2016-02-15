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
package com.btkelly.gnag.utils;

import org.jetbrains.annotations.NotNull;

public class HtmlStringBuilder {

    private static final String BOLD_OPEN_TAG  = "<b>";
    private static final String BOLD_CLOSE_TAG = "</b>";
    private static final String LINE_BREAK_TAG = "<br />";

    private final StringBuilder stringBuilder;

    public HtmlStringBuilder() {
        stringBuilder = new StringBuilder();
    }

    public HtmlStringBuilder append(@NotNull final CharSequence string) {
        stringBuilder.append(string);
        return this;
    }

    public HtmlStringBuilder appendLink(@NotNull final CharSequence string, @NotNull final String linkUrl) {
        stringBuilder
                .append("<a href=\"")
                .append(linkUrl)
                .append("\">")
                .append(string)
                .append("</a>");
        return this;
    }

    public HtmlStringBuilder appendBold(@NotNull final CharSequence string) {
        stringBuilder
                .append(BOLD_OPEN_TAG)
                .append(string)
                .append(BOLD_CLOSE_TAG);
        return this;
    }

    public HtmlStringBuilder insertLineBreak() {
        stringBuilder.append(LINE_BREAK_TAG);
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}
