package com.btkelly.gnag.utils

/**
 * Created by bobbake4 on 10/23/15.
 */
class XMLUtil {

    public static String cleanseXMLString(String cleanseMe) {
        return cleanseMe.replaceAll("\\[", "")
                .replaceAll("\\]","")
                .replaceAll("\n", "");
    }
}
