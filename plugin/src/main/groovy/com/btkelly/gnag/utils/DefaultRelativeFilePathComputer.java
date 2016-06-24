package com.btkelly.gnag.utils;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DefaultRelativeFilePathComputer {

    /**
     * @param absoluteFilePath the absolute path to a target file. Should be trimmed of all excess whitespace and
     *                         newlines.
     * @param project the current project
     * @return the path to the target file, relative to the root directory of the current project, iff the target file
     *         is contained within that directory; null otherwise
     */
    @Nullable
    default String computeFilePathRelativeToProjectRoot(
            @NotNull final String absoluteFilePath, @NotNull final Project project) {
        
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
