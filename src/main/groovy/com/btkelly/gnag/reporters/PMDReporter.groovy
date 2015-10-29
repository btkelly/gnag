package com.btkelly.gnag.reporters

import com.btkelly.gnag.utils.XMLUtil
import org.gradle.api.Project

/**
 * Created by bobbake4 on 10/23/15.
 */
class PMDReporter implements CommentReporter {

    @Override
    boolean shouldFailBuild(Project project) {
        Node pmdXMLTree = getPMDXMLTree(project);
        return pmdXMLTree.children().size() != 0;
    }

    @Override
    String textToAppendComment(Project project) {

        String projectDir = project.projectDir;

        println "Parsing PMD violations";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PMD Violations:")

        Node pmdXMLTree = getPMDXMLTree(project);

        pmdXMLTree.file.each { file ->

            String helpURL = XMLUtil.cleanseXMLString((String) file.violation.@externalInfoUrl);
            String lineNumber = XMLUtil.cleanseXMLString((String) file.violation.@beginline);
            String violationText = XMLUtil.cleanseXMLString((String) file.violation.text());
            String violationRule = XMLUtil.cleanseXMLString((String) file.violation.@rule);

            String fileName = file.@name;
            fileName = fileName.replace(projectDir, "");
            fileName = fileName.replace("/src/main/java/", "");
            fileName = fileName.replace("/", ".");

            stringBuilder.append("\\n----------------------------------\\n");
            stringBuilder.append("<b>Violation: </b> " + violationRule);
            stringBuilder.append("\\n");
            stringBuilder.append("<b>Help: </b> " + helpURL);
            stringBuilder.append("\\n");
            stringBuilder.append("<b>Class: </b>" + fileName);
            stringBuilder.append(" - ");
            stringBuilder.append(" <b>Line: </b>" + lineNumber);
            stringBuilder.append("\\n");
            stringBuilder.append(violationText);

        }

        println "Finished parsing PMD violations";

        return stringBuilder.toString();
    }

    @Override
    String reporterName() {
        return "PMD Reporter"
    }

    private Node getPMDXMLTree(Project project) {
        File pmdFile = getPMDReportFile(project);
        return new XmlParser().parse(pmdFile);
    }

    private File getPMDReportFile(Project project) {
        return new File(project.projectDir, "/build/outputs/pmd/pmd.xml");
    }
}
