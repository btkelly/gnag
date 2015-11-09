package com.btkelly.gnag.reporters

import com.btkelly.gnag.utils.XMLUtil
import org.gradle.api.Project

/**
 * Comment reporter for Checkstyle. Looks for Checkstyle report in the default location "/build/outputs/checkstyle/checkstyle.xml"
 */
class CheckstyleReporter implements CommentReporter {

    /**
     * Looks through the Checkstyle report and determines if any file node has children.
     * @param project - current project being built
     * @return - true if any file node has children
     */
    @Override
    boolean shouldFailBuild(Project project) {
        Node checkstyleXMLTree = getCheckstyleXMLTree(project)

        for (Object file : checkstyleXMLTree.children()) {
            if (file.children().size() > 0) {
                return true
            }
        }

        return false
    }

    /**
     * Loops through all Checkstyle errors and pulls out file name, line number, error message, and rule
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    @Override
    String textToAppendComment(Project project) {

        StringBuilder stringBuilder = new StringBuilder();

        println "Parsing Checkstyle violations";

        if (shouldFailBuild(project)) {

            stringBuilder.append("Checkstyle Violations:")
            stringBuilder.append("\\n----------------------------------\\n");

            Node checkstyleXMLTree = getCheckstyleXMLTree(project)

            checkstyleXMLTree.file.each { Node file ->

                file.error.each { Node error ->

                    String lineNumber = XMLUtil.cleanseXMLString((String) error.@line);
                    String violationText = XMLUtil.cleanseXMLString((String) error.@message);
                    String violationRule = XMLUtil.cleanseXMLString((String) error.@source);

                    String projectDir = project.projectDir;
                    String fileName = file.@name;
                    fileName = fileName.replace(projectDir, "");
                    fileName = fileName.replace("/src/main/java/", "");
                    fileName = fileName.replace("/", ".");

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
        }

        println "Finished parsing Checkstyle violations";

        return stringBuilder.toString();
    }

    @Override
    String reporterName() {
        return "Checkstyle Reporter"
    }

    private Node getCheckstyleXMLTree(Project project) {
        File checkstyleFile = getCheckstyleReportFile(project);
        return new XmlParser().parse(checkstyleFile);
    }

    private File getCheckstyleReportFile(Project project) {
        return new File(project.projectDir, "/build/outputs/checkstyle/checkstyle.xml");
    }
}
