/**
 *    Copyright 2013-2015 Tom Hombergs (tom.hombergs@gmail.com | http://wickedsource.org)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.stkent.githubdiffparser.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Diff between two files.
 *
 * @author Tom Hombergs [tom.hombergs@gmail.com]
 */
public class Diff {
    
    public static final int NUMBER_OF_LINES_PER_DELIMITER = 1;

    private String fromFileName;

    private String toFileName;

    private List<String> headerLines = new ArrayList<>();

    private List<Hunk> hunks = new ArrayList<>();
    
    /**
     * The header lines of the diff. These lines are purely informational and are not parsed.
     *
     * @return the list of header lines.
     */
    public List<String> getHeaderLines() {
        return headerLines;
    }

    /**
     * Gets the name of the first file that was compared with this Diff (the file "from" which the changes were made,
     * i.e. the "left" file of the diff).
     *
     * @return the name of the "from"-file.
     */
    public String getFromFileName() {
        return fromFileName;
    }

    /**
     * Gets the name of the second file that was compared with this Diff (the file "to" which the changes were made,
     * i.e. the "right" file of the diff).
     *
     * @return the name of the "to"-file.
     */
    public String getToFileName() {
        return toFileName;
    }

    /**
     * The list if all {@link Hunk}s which contain all changes that are part of this Diff.
     *
     * @return list of all Hunks that are part of this Diff.
     */
    public List<Hunk> getHunks() {
        return hunks;
    }

    public void setFromFileName(String fromFileName) {
        this.fromFileName = fromFileName;
    }

    public void setToFileName(String toFileName) {
        this.toFileName = toFileName;
    }

    /**
     * Gets the last {@link Hunk} of changes that is part of this Diff.
     *
     * @return the last {@link Hunk} that has been added to this Diff.
     */
    public Hunk getLatestHunk() {
        return hunks.get(hunks.size() - 1);
    }
    
    public boolean isNotEmpty() {
        return !headerLines.isEmpty() || !hunks.isEmpty();
    }

    /**
     * NOTE: result is based on the first line being labelled line number 1, not line number 0!
     */
    @Nullable
    public Integer getDiffLineNumberForToFileLocation(
            @NotNull final String toFileName,
            final int toFileLineNumber) {
        
        if (!toFileName.equals(this.toFileName)) {
            return null;
        }

        int diffLineNumber = headerLines.size() + NUMBER_OF_LINES_PER_DELIMITER;
        
        int currentHunkIndex = 0;
        while (currentHunkIndex < hunks.size()) {
            diffLineNumber = diffLineNumber + Hunk.NUMBER_OF_LINES_PER_DELIMITER;
            
            final Hunk currentHunk = hunks.get(currentHunkIndex);
            
            if (currentHunk.containsToFileLineNumber(toFileLineNumber)) {
                //noinspection ConstantConditions
                return diffLineNumber + currentHunk.getHunkLineNumberForToFileLineNumber(toFileLineNumber);
            } else {
                diffLineNumber += currentHunk.getNumberOfLines();
            }
            
            currentHunkIndex++;
        }
        
        return null;
    }
    
}
