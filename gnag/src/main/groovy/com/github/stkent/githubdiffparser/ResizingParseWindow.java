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

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link ResizingParseWindow} slides through the lines of a input stream and
 * offers methods to get the currently focused line as well as upcoming lines.
 * It is backed by an automatically resizing {@link LinkedList}
 *
 * @author Tom Hombergs [tom.hombergs@gmail.com]
 */
@SuppressWarnings("UnusedDeclaration")
public class ResizingParseWindow implements ParseWindow {

    private BufferedReader reader;

    private LinkedList<String> lineQueue = new LinkedList<>();

    private int lineNumber = 0;

    private List<Pattern> ignorePatterns = new ArrayList<>();

    private boolean isEndOfStream = false;

    public ResizingParseWindow(InputStream in) {
        Reader unbufferedReader = new InputStreamReader(in);
        this.reader = new BufferedReader(unbufferedReader);
    }

    public void addIgnorePattern(String ignorePattern) {
        this.ignorePatterns.add(Pattern.compile(ignorePattern));
    }

    @Override
    public String getFutureLine(int distance) {
        try {
            resizeWindowIfNecessary(distance + 1);
            return lineQueue.get(distance);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void addLine(int pos, String line) {
        lineQueue.add(pos, line);
    }

    /**
     * Resizes the sliding window to the given size, if necessary.
     *
     * @param newSize the new size of the window (i.e. the number of lines in the
     *                window).
     */
    private void resizeWindowIfNecessary(int newSize) {
        try {
            int numberOfLinesToLoad = newSize - this.lineQueue.size();
            for (int i = 0; i < numberOfLinesToLoad; i++) {
                String nextLine = getNextLine();
                if (nextLine != null) {
                    lineQueue.addLast(nextLine);
                } else {
                    throw new IndexOutOfBoundsException("End of stream has been reached!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String slideForward() {
        try {
            lineQueue.pollFirst();
            lineNumber++;
            if (lineQueue.isEmpty()) {
                String nextLine = getNextLine();
                if (nextLine != null) {
                    lineQueue.addLast(nextLine);
                }
                return nextLine;
            } else {
                return lineQueue.peekFirst();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getNextLine() throws IOException {
        String nextLine = reader.readLine();
        while (matchesIgnorePattern(nextLine)) {
            nextLine = reader.readLine();
        }

        return nextLine;
    }

    private boolean matchesIgnorePattern(String line) {
        if (line == null) {
            return false;
        } else {
            for (Pattern pattern : ignorePatterns) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String getFocusLine() {
        return lineQueue.element();
    }

    @Override
    public int getFocusLineNumber() {
        return lineNumber;
    }

}
