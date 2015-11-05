package com.btkelly.gnag.reporters

import com.btkelly.gnag.utils.XMLUtil
import org.gradle.api.Project

/**
 * Created by bobbake4 on 11/5/15.
 */
class FindbugsReporter implements CommentReporter {

    @Override
    boolean shouldFailBuild(Project project) {
        Node findBugsXMLTree = getFindbugsXMLTree(project);
        NodeList summary = findBugsXMLTree.get("FindBugsSummary") as NodeList
        println findBugsXMLTree.get("FindBugsSummary").get(0).attribute("total_bugs");
        return summary.get(0).attribute("total_bugs") != 0;
    }

    @Override
    String textToAppendComment(Project project) {

        println "Parsing Findbugs violations";

        Node findBugsXMLTree = getFindbugsXMLTree(project);
        NodeList bugs = findBugsXMLTree.get("BugInstance") as NodeList

        StringBuilder stringBuilder = new StringBuilder();

        if (bugs.size() > 0 ) {

            stringBuilder.append("Findbugs Violations:")
            stringBuilder.append("\\n----------------------------------\\n");

            bugs.each { bug ->

                String lineNumber = XMLUtil.cleanseXMLString((String) bug.SourceLine.@start);
                String violationText = XMLUtil.cleanseXMLString((String) bug.ShortMessage.text());
                String violationRule = XMLUtil.cleanseXMLString((String) bug.@type);
                String fileName = XMLUtil.cleanseXMLString((String) bug.SourceLine.@classname);

                stringBuilder.append("<b>Violation: </b> " + violationRule);
                stringBuilder.append("\\n");
                stringBuilder.append("<b>Class: </b>" + fileName);
                stringBuilder.append(" - ");
                stringBuilder.append(" <b>Line: </b>" + lineNumber);
                stringBuilder.append("\\n");
                stringBuilder.append(violationText);
                stringBuilder.append("\\n\\n");
            }
        }

        println "Finished parsing Findbugs violations";

        return stringBuilder.toString();
    }

    @Override
    String reporterName() {
        return "FindBugs Reporter";
    }

    private Node getFindbugsXMLTree(Project project) {
        File findbugsFile = getFindbugsReportFile(project);
        return new XmlParser().parse(findbugsFile);
    }

    private File getFindbugsReportFile(Project project) {
        return new File(project.projectDir, "/build/outputs/findbugs/findbugs.xml");
    }
}
