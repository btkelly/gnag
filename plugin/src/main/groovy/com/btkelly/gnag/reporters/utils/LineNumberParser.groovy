package com.btkelly.gnag.reporters.utils

final class LineNumberParser {

    /**
     * @param lineNumberString a String we would like to parse into an integer value.
     * @param reporterName the name of the reporter currently running. Used for logging only.
     * @param violationType the name of the violation currently being parsed. Used for logging only.
     * @return a positive integer, if lineNumberString can be parsed into such a value; null in any other case.
     */
    static Integer parseLineNumberString(
            final String lineNumberString,
            final String reporterName,
            final String violationType) {

        if (lineNumberString.isEmpty()) {
            return null
        } else {
            try {
                final int result = lineNumberString.toInteger()

                if (result >= 0) {
                    return result
                } else {
                    System.out.println(
                            "Invalid line number: + " + result +
                                    " for " + reporterName + " violation: " + violationType)

                    return null
                }
            } catch (final NumberFormatException ignored) {
                System.out.println(
                        "Error parsing line number string: \"" + lineNumberString +
                                "\" for " + reporterName + " violation: " + violationType)

                return null
            }
        }
    }

    private LineNumberParser() {
        // This constructor intentionally left blank.
    }

}
