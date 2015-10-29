package com.btkelly.gnag.reporters

import org.gradle.api.Project

/**
 * Created by bobbake4 on 10/23/15.
 */
interface CommentReporter {

    boolean shouldFailBuild(Project project);
    String textToAppendComment(Project project);
    String reporterName();

}