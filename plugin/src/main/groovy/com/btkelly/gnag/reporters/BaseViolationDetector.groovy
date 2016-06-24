package com.btkelly.gnag.reporters

import org.gradle.api.Project

abstract class BaseViolationDetector implements ViolationDetector {

    protected final Project project

    BaseViolationDetector(final Project project) {
        this.project = project
    }

    /**
     * @param absoluteFilePath the absolute path to a target file. Should be trimmed of all excess whitespace and
     *                         newlines.
     * @param project the current project
     * @return the path to the target file, relative to the root directory of the current project, iff the target file
     *         is contained within that directory; null otherwise
     */
    protected String computeFilePathRelativeToProjectRoot(final String absoluteFilePath) {

        final String expectedFilePathPrefix = project.getRootDir().getAbsolutePath() + "/";

        if (!absoluteFilePath.startsWith(expectedFilePathPrefix)) {
            // The target file _is not_ contained within the root directory of the current project. Return null.
            return null;
        } else {
            /*
             * The target file _is_ contained within the root directory of the current project. Return the path to the
             * target file relative to the root directory.
             */
            return absoluteFilePath.replace(expectedFilePathPrefix, "");
        }
    }
    
}
