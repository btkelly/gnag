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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.stkent.githubdiffparser.Constants.HUNK_START_PATTERN;

/**
 * State machine for a parser parsing a unified diff.
 *
 * @author Tom Hombergs [tom.hombergs@gmail.com]
 */
@SuppressWarnings("Duplicates")
enum ParserState {

    /**
     * The parser is in this state initially.
     */
    INITIAL {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();

            if (matchesDiffStartPattern(line)) {
                return transition(line, DIFF_START, logToSout);
            } else {
                return null;
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing the first line of a new diff.
     */
    DIFF_START {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();
            
            if (matchesFromFilePattern(line)) {
                return transition(line, FROM_FILE, logToSout);
            } else {
                return transition(line, HEADER, logToSout);
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing a header line.
     */
    HEADER {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();
            
            if (matchesDiffStartPattern(line)) {
                return transition(line, DIFF_START, logToSout);
            } else if (matchesFromFilePattern(line)) {
                return transition(line, FROM_FILE, logToSout);
            } else {
                return transition(line, HEADER, logToSout);
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing the line containing the "from" file.
     * 
     * Example line:
     * 
     * {@code --- /path/to/file.txt}
     */
    FROM_FILE {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();
            
            if (matchesToFilePattern(line)) {
                return transition(line, TO_FILE, logToSout);
            } else {
                throw new IllegalStateException("A FROM_FILE line ('---') must be directly followed by a TO_FILE line ('+++')!");
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing the line containing the "to" file.
     * 
     * Example line:
     * 
     * {@code +++ /path/to/file.txt}
     */
    TO_FILE {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();
            
            if (matchesHunkStartPattern(line)) {
                return transition(line, HUNK_START, logToSout);
            } else {
                throw new IllegalStateException("A TO_FILE line ('+++') must be directly followed by a HUNK_START line ('@@')!");
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing a line containing the header of a hunk.
     * 
     * Example line:
     * 
     * {@code @@ -1,5 +2,6 @@}
     */
    HUNK_START {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();
            
            if (matchesFromLinePattern(line)) {
                return transition(line, FROM_LINE, logToSout);
            } else if (matchesToLinePattern(line)) {
                return transition(line, TO_LINE, logToSout);
            } else {
                return transition(line, NEUTRAL_LINE, logToSout);
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing a line containing a line that is in the first file,
     * but not the second (a "from" line).
     * 
     * Example line:
     * 
     * {@code - only the dash at the start is important}
     */
    FROM_LINE {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();

            if (matchesDiffStartPattern(line)) {
                return transition(line, DIFF_START, logToSout);
            } else if (matchesFromLinePattern(line)) {
                return transition(line, FROM_LINE, logToSout);
            } else if (matchesToLinePattern(line)) {
                return transition(line, TO_LINE, logToSout);
            } else if (matchesNeutralLinePattern(line) || matchesNoNewlineAtEndOfFileLinePattern(line)) {
                return transition(line, NEUTRAL_LINE, logToSout);
            } else if (matchesHunkStartPattern(line)) {
                return transition(line, HUNK_START, logToSout);
            } else {
                return transition(line, HEADER, logToSout);
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing a line containing a line that is in the second file,
     * but not the first (a "to" line).
     * 
     * Example line:
     * 
     * {@code + only the plus at the start is important}
     */
    TO_LINE {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();

            if (matchesDiffStartPattern(line)) {
                return transition(line, DIFF_START, logToSout);
            } else if (matchesFromLinePattern(line)) {
                return transition(line, FROM_LINE, logToSout);
            } else if (matchesToLinePattern(line)) {
                return transition(line, TO_LINE, logToSout);
            } else if (matchesNeutralLinePattern(line) || matchesNoNewlineAtEndOfFileLinePattern(line)) {
                return transition(line, NEUTRAL_LINE, logToSout);
            } else if (matchesHunkStartPattern(line)) {
                return transition(line, HUNK_START, logToSout);
            } else {
                return transition(line, HEADER, logToSout);
            }
        }
    },

    /**
     * The parser is in this state if it is currently parsing a line that is contained in both files (a "neutral" line). This line can
     * contain any string.
     */
    NEUTRAL_LINE {
        @Nullable
        @Override
        public ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout) {
            String line = window.getFocusLine();

            if (matchesDiffStartPattern(line)) {
                return transition(line, DIFF_START, logToSout);
            } else if (matchesFromLinePattern(line)) {
                return transition(line, FROM_LINE, logToSout);
            } else if (matchesToLinePattern(line)) {
                return transition(line, TO_LINE, logToSout);
            } else if (matchesNeutralLinePattern(line) || matchesNoNewlineAtEndOfFileLinePattern(line)) {
                return transition(line, NEUTRAL_LINE, logToSout);
            } else if (matchesHunkStartPattern(line)) {
                return transition(line, HUNK_START, logToSout);
            } else {
                return transition(line, HEADER, logToSout);
            }
        }
    };

    /**
     * Returns the next state of the state machine depending on the current state and the content of a window of lines around the line
     * that is currently being parsed.
     *
     * @param window the window around the line currently being parsed.
     * @return the next valid state of the state machine; null if no valid state can be deduced.
     */
    @Nullable
    public abstract ParserState nextState(@NotNull final ParseWindow window, final boolean logToSout);

    protected ParserState transition(final String currentLine, final ParserState toState, final boolean logToSout) {
        if (logToSout) {
            System.out.println(String.format("%12s -> %12s: %s", this, toState, currentLine));
        }
        
        return toState;
    }

    protected boolean matchesDiffStartPattern(@NotNull final String line) {
        return line.startsWith("diff --git");
    }

    protected boolean matchesFromFilePattern(@NotNull final String line) {
        return line.startsWith("---");
    }

    protected boolean matchesToFilePattern(@NotNull final String line) {
        return line.startsWith("+++");
    }

    protected boolean matchesFromLinePattern(@NotNull final String line) {
        return line.startsWith("-");
    }

    protected boolean matchesToLinePattern(@NotNull final String line) {
        return line.startsWith("+");
    }
    
    protected boolean matchesNeutralLinePattern(@NotNull final String line) {
        return line.startsWith(" ");
    }

    protected boolean matchesNoNewlineAtEndOfFileLinePattern(@NotNull final String line) {
        return line.contains("\\ No newline at end of file");
    }

    protected boolean matchesHunkStartPattern(@NotNull final String line) {
        return HUNK_START_PATTERN.matcher(line).matches();
    }

}
