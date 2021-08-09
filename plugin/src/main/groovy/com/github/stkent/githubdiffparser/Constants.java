package com.github.stkent.githubdiffparser;

import java.util.regex.Pattern;

public class Constants {

    public static final Pattern HUNK_START_PATTERN = Pattern.compile("^.*-([0-9]+)(?:,([0-9]+))? \\+([0-9]+)(?:,([0-9]+))?.*$");

}
