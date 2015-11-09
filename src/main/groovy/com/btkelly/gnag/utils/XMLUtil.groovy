package com.btkelly.gnag.utils

/**
 * Created by bobbake4 on 10/23/15.
 */
class XMLUtil {

    /**
     * The XML parser inserts some unwanted brackets and newlines, this method removes them from the supplied string
     * @param cleanseMe
     * @return
     */
    public static String cleanseXMLString(String cleanseMe) {
        return cleanseMe.replaceAll("\\[", "")
                .replaceAll("\\]","")
                .replaceAll("\n", "");
    }
}
