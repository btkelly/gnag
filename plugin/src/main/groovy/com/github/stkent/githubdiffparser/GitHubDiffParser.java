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

import com.github.stkent.githubdiffparser.models.Diff;
import com.github.stkent.githubdiffparser.models.Hunk;
import com.github.stkent.githubdiffparser.models.Line;
import com.github.stkent.githubdiffparser.models.Range;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public class GitHubDiffParser {
    
    private final boolean logToSout;

    public GitHubDiffParser() {
        this(false);
    }

    public GitHubDiffParser(final boolean logToSout) {
        this.logToSout = logToSout;
    }

    @NotNull
    public List<Diff> parse(InputStream in) {
        ResizingParseWindow window = new ResizingParseWindow(in);
        ParserState state = ParserState.INITIAL;
        List<Diff> parsedDiffs = new ArrayList<>();
        Diff currentDiff = new Diff();
        String currentLine;

        while ((currentLine = window.slideForward()) != null) {
            state = state.nextState(window, logToSout);
            
            if (state == null) {
                throw new IllegalStateException("Parser reached illegal state!");
            }
            
            switch (state) {
                case DIFF_START:
                    if (currentDiff.isNotEmpty()) {
                        parsedDiffs.add(currentDiff);
                    }
                    
                    currentDiff = new Diff();
                    break;
                case HEADER:
                    parseHeader(currentDiff, currentLine);
                    break;
                case FROM_FILE:
                    parseFromFile(currentDiff, currentLine);
                    break;
                case TO_FILE:
                    parseToFile(currentDiff, currentLine);
                    break;
                case HUNK_START:
                    parseHunkStart(currentDiff, currentLine);
                    break;
                case FROM_LINE:
                    parseFromLine(currentDiff, currentLine);
                    break;
                case TO_LINE:
                    parseToLine(currentDiff, currentLine);
                    break;
                case NEUTRAL_LINE:
                    parseNeutralLine(currentDiff, currentLine);
                    break;
            }
        }

        if (currentDiff.isNotEmpty()) {
            parsedDiffs.add(currentDiff);
        }
        
        return parsedDiffs;
    }

    @NotNull
    public List<Diff> parse(byte[] bytes) {
        return parse(new ByteArrayInputStream(bytes));
    }

    @NotNull
    public List<Diff> parse(File file) throws IOException {
        return parse(new FileInputStream(file));
    }

    private void parseNeutralLine(Diff currentDiff, String currentLine) {
        Line line = new Line(Line.LineType.NEUTRAL, currentLine);
        currentDiff.getLatestHunk().getLines().add(line);
    }

    private void parseToLine(Diff currentDiff, String currentLine) {
        Line toLine = new Line(Line.LineType.TO, currentLine.substring(1));
        currentDiff.getLatestHunk().getLines().add(toLine);
    }

    private void parseFromLine(Diff currentDiff, String currentLine) {
        Line fromLine = new Line(Line.LineType.FROM, currentLine.substring(1));
        currentDiff.getLatestHunk().getLines().add(fromLine);
    }

    private void parseHunkStart(Diff currentDiff, String currentLine) {
        Matcher matcher = Constants.HUNK_START_PATTERN.matcher(currentLine);
        
        if (matcher.matches()) {
            String range1Start = matcher.group(1);
            String range1Count = (matcher.group(2) != null) ? matcher.group(2) : "1";
            Range fromRange = new Range(Integer.valueOf(range1Start), Integer.valueOf(range1Count));

            String range2Start = matcher.group(3);
            String range2Count = (matcher.group(4) != null) ? matcher.group(4) : "1";
            Range toRange = new Range(Integer.valueOf(range2Start), Integer.valueOf(range2Count));

            Hunk hunk = new Hunk();
            hunk.setFromFileRange(fromRange);
            hunk.setToFileRange(toRange);
            currentDiff.getHunks().add(hunk);
        } else {
            throw new IllegalStateException(String.format("No line ranges found in the following hunk start line: '%s'. Expected something " +
                    "like '-1,5 +3,5'.", currentLine));
        }
    }

    private void parseFromFile(final Diff currentDiff, final String currentLine) {
        String fileName = cutAfterTab(currentLine.substring(4)).trim();
        
        /* 
         * GitHub diff "from file" rows include an a/ prefix. We remove this to compute the actual (relative) path to
         * the file.
         */
        if (fileName.startsWith("a/")) {
            fileName = fileName.substring(2);
        }

        currentDiff.setFromFileName(fileName);
    }

    private void parseToFile(final Diff currentDiff, final String currentLine) {
        String fileName = cutAfterTab(currentLine.substring(4)).trim();
        
        /* 
         * GitHub diff "to file" rows include a b/ prefix. We remove this to compute the actual (relative) path to the
         * file.
         */
        if (fileName.startsWith("b/")) {
            fileName = fileName.substring(2);
        }

        currentDiff.setToFileName(fileName);
    }

    /**
     * Cuts a TAB and all following characters from a String.
     */
    private String cutAfterTab(String line) {
        Pattern p = Pattern.compile("^(.*)\\t.*$");
        Matcher matcher = p.matcher(line);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return line;
        }
    }

    private void parseHeader(Diff currentDiff, String currentLine) {
        currentDiff.getHeaderLines().add(currentLine);
    }

}
