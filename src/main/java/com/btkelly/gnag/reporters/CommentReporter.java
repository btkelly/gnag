package com.btkelly.gnag.reporters;

import org.gradle.api.Project;

/**
 * Interface for a comment reporter
 */
public interface CommentReporter {

    /**
     * This method should inspect the current project and return a true or false if this reporter should cause the build to fail.
     * @param project - current project being built
     * @return - return true to fail the current build false otherwise
     */
    boolean shouldFailBuild(Project project);

    /**
     * This method should generate the text it would like added to the Github comment that will be posted at the end of the build.
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    String textToAppendComment(Project project);

    /**
     * Return the reporter name
     * @return - reporter name
     */
    String reporterName();
}
