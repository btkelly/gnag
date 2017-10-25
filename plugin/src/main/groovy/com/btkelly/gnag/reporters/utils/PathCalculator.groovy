package com.btkelly.gnag.reporters.utils

import org.gradle.api.Project

import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull

final class PathCalculator {

    /**
     * @param project the current project
     * @param absoluteFilePath the absolute path to a target file. Should be trimmed of all excess whitespace and
     *                         newlines. Can be null.
     * @return the path to the target file, relative to the root directory of the current project, iff the target file
     *         is contained within that directory; null otherwise
     */
    static String calculatePathWithinProject(final Project project, final String absoluteFilePath) {
        final String sanitizedAbsoluteFilePath = sanitizeToNonNull(absoluteFilePath)

        final String expectedFilePathPrefix = project.getRootDir().getAbsolutePath() + "/"

        if (!sanitizedAbsoluteFilePath.startsWith(expectedFilePathPrefix)) {
            // The target file _is not_ contained within the root directory of the current project. Return null.
            return null
        } else {
            /*
             * The target file _is_ contained within the root directory of the current project. Return the path to the
             * target file relative to the root directory.
             */
            return sanitizedAbsoluteFilePath.replace(expectedFilePathPrefix, "")
        }
    }

}
