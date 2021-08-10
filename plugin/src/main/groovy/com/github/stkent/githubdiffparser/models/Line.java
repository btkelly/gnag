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
 * Represents a line of a Diff. A line is either contained in both files ("neutral"), only in the first file ("from"),
 * or only in the second file ("to").
 *
 * @author Tom Hombergs [tom.hombergs@gmail.com]
 */
@SuppressWarnings("UnusedDeclaration")
public class Line {

    /**
     * All possible types a line can have.
     */
    public enum LineType {

        /**
         * This line is only contained in the first file of the Diff (the "from" file).
         */
        FROM,

        /**
         * This line is only contained in the second file of the Diff (the "to" file).
         */
        TO,

        /**
         * This line is contained in both filed of the Diff, and is thus considered "neutral".
         */
        NEUTRAL

    }

    private final LineType lineType;

    private final String content;

    public Line(LineType lineType, String content) {
        this.lineType = lineType;
        this.content = content;
    }

    /**
     * The type of this line.
     *
     * @return the type of this line.
     */
    public LineType getLineType() {
        return lineType;
    }

    /**
     * The actual content of the line as String.
     *
     * @return the actual line content.
     */
    public String getContent() {
        return content;
    }
    
    // Generated equals and hashCode

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Line line = (Line) o;

        if (lineType != line.lineType) return false;
        return content != null ? content.equals(line.content) : line.content == null;

    }

    @Override
    public int hashCode() {
        int result = lineType != null ? lineType.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
