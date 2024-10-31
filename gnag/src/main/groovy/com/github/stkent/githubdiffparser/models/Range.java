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

/**
 * Represents a range of line numbers that spans a window on a text file.
 *
 * @author Tom Hombergs [tom.hombergs@gmail.com]
 */
public class Range {

    private final int lineStart;

    private final int lineCount;

    public Range(int lineStart, int lineCount) {
        this.lineStart = lineStart;
        this.lineCount = lineCount;
    }

    /**
     * @return the line number at which this range starts (inclusive).
     */
    public int getLineStart() {
        return lineStart;
    }

    /**
     * @return the count of lines in this range.
     */
    public int getLineCount() {
        return lineCount;
    }

    /**
     * @param lineNumber - line number
     * @return true if the supplied line number lies inside this range; false otherwise
     */
    public boolean contains(final int lineNumber) {
        return lineStart <= lineNumber && lineNumber <= getLineEnd();
    }

    /**
     * @return the line number at which this range ends (inclusive).
     */
    private int getLineEnd() {
        return lineStart + lineCount - 1;
    }

}
