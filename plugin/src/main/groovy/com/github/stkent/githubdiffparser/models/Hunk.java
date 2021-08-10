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

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a "hunk" of changes made to a file.
 * 
 * A Hunk consists of one or more lines that either exist only in the first file ("from line"), only in the second file ("to line") or in
 * both files ("neutral line"). Additionally, it contains information about which excerpts of the compared files are compared in this
 * Hunk in the form of line ranges.
 *
 * @author Tom Hombergs [tom.hombergs@gmail.com]
 */
public class Hunk {
    
    public static final int NUMBER_OF_LINES_PER_DELIMITER = 1;

    private Range fromFileRange;

    private Range toFileRange;

    private List<Line> lines = new ArrayList<>();

    /**
     * The range of line numbers that this Hunk spans in the first file of the Diff.
     *
     * @return range of line numbers in the first file (the "from" file).
     */
    public Range getFromFileRange() {
        return fromFileRange;
    }

    /**
     * The range of line numbers that this Hunk spans in the second file of the Diff.
     *
     * @return range of line numbers in the second file (the "to" file).
     */
    public Range getToFileRange() {
        return toFileRange;
    }

    /**
     * The lines that are part of this Hunk.
     *
     * @return lines of this Hunk.
     */
    public List<Line> getLines() {
        return lines;
    }

    protected void setLines(final List<Line> lines) {
        this.lines = lines;
    }

    public void setFromFileRange(Range fromFileRange) {
        this.fromFileRange = fromFileRange;
    }

    public void setToFileRange(Range toFileRange) {
        this.toFileRange = toFileRange;
    }

    /**
     * @return the total number of lines in this Hunk (does not include hunk header line)
     */
    public int getNumberOfLines() {
        return lines.size();
    }
    
    public boolean containsToFileLineNumber(final int toFileLineNumber) {
        return toFileRange.contains(toFileLineNumber);
    }

    /**
     * NOTE: result is based on the first line being labelled line number 1, not line number 0!
     *
     * @param toFileLineNumber - to file line number
     * @return - hunk line number
     */
    @Nullable
    public Integer getHunkLineNumberForToFileLineNumber(final int toFileLineNumber) {
        if (!containsToFileLineNumber(toFileLineNumber)) {
            return null;
        }
        
        int currentLineNumber = 1;
        int currentToLineNumber = toFileRange.getLineStart();
        
        while (currentLineNumber <= lines.size()) {
            /*
             * Lines of types "TO" and "NEUTRAL" are both present in the second file.
             * Only lines of type "FROM" are not present in the second file.
             */
            if (lines.get(currentLineNumber - 1).getLineType() != Line.LineType.FROM) {
                if (currentToLineNumber == toFileLineNumber) {
                    return currentLineNumber;
                }
                
                currentToLineNumber += 1;
            }
            
            currentLineNumber++;
        }

        throw new IllegalStateException("This code path should never be exercised.");
    }
    
}
