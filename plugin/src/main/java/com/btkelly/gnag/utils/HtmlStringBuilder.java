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
