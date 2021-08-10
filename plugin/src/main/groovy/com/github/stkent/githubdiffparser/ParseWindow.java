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
package com.github.stkent.githubdiffparser;

public interface ParseWindow {

    /**
     * Returns the line currently focused by this window. This is actually the
     * same line as returned by {@link #slideForward()} but calling
     * this method does not slide the window forward a step.
     *
     * @return the currently focused line.
     */
    String getFocusLine();

    /**
     * Returns the number of the current line within the whole document.
     *
     * @return the line number.
     */
    @SuppressWarnings("UnusedDeclaration")
    int getFocusLineNumber();

    /**
     * Slides the window forward one line.
     *
     * @return the next line that is in the focus of this window or null if the
     * end of the stream has been reached.
     */
    String slideForward();

    /**
     * Looks ahead from the current line and retrieves a line that will be the
     * focus line after the window has slided forward.
     *
     * @param distance the number of lines to look ahead. Must be greater or equal 0.
     *                 0 returns the focus line. 1 returns the first line after the
     *                 current focus line and so on. Note that all lines up to the
     *                 returned line will be held in memory until the window has
     *                 slided past them, so be careful not to look ahead too far!
     * @return the line identified by the distance parameter that lies ahead of
     *         the focus line. Returns null if the line cannot be read because
     *         it lies behind the end of the stream.
     */
    String getFutureLine(int distance);

    void addLine(int pos, String line);

}
