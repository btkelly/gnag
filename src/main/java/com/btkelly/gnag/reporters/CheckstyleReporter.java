package com.btkelly.gnag.reporters;

import com.btkelly.gnag.models.checkstyle.Checkstyle;
import com.btkelly.gnag.models.checkstyle.Error;
import com.btkelly.gnag.models.checkstyle.File;
import org.gradle.api.Project;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

/**
 * Comment reporter for Checkstyle. Looks for Checkstyle report in the default location "/build/outputs/checkstyle/checkstyle.xml"
 */
public class CheckstyleReporter implements CommentReporter {

    /**
     * Looks through the Checkstyle report and determines if any file node has children.
     * @param project - current project being built
     * @return - true if any file node has children
     */
    @Override
    public boolean shouldFailBuild(Project project) {
        try {
            return getCheckstyleReport(project).shouldFailBuild();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Loops through all Checkstyle errors and pulls out file name, line number, error message, and rule
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    @Override
    public String textToAppendComment(Project project) {

        StringBuilder stringBuilder = new StringBuilder();

        //println"Parsing Checkstyle violations";

        try {
            Checkstyle checkstyleReport = getCheckstyleReport(project);

            if (checkstyleReport.shouldFailBuild()) {

                stringBuilder.append("Checkstyle Violations:");
                stringBuilder.append("\\n----------------------------------\\n");

                for (File checkstyleFile : checkstyleReport.getFile()) {

                    String projectDir = project.getProjectDir().toString();

                    String fileName = checkstyleFile.getName();
                    fileName = fileName.replace(projectDir, "");
                    fileName = fileName.replace("/src/main/java/", "");
                    fileName = fileName.replace("/", ".");

                    for (Error checkstyleError : checkstyleFile.getError()) {

                        stringBuilder.append("<b>Violation: </b> " + checkstyleError.getSource());
                        stringBuilder.append("\\n");
                        stringBuilder.append("<b>Class: </b>" + fileName);
                        stringBuilder.append(" - ");
                        stringBuilder.append(" <b>Line: </b>" + checkstyleError.getLine());
                        stringBuilder.append("\\n");
                        stringBuilder.append(checkstyleError.getMessage());
                        stringBuilder.append("\\n\\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //println "Finished parsing Checkstyle violations";

        return stringBuilder.toString();
    }

    @Override
    public String reporterName() {
        return "Checkstyle Reporter";
    }

    private Checkstyle getCheckstyleReport(Project project) throws IOException, JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Checkstyle.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return (Checkstyle) unmarshaller.unmarshal(getCheckstyleReportFile(project));
    }

    private java.io.File getCheckstyleReportFile(Project project) {
        return new java.io.File(project.getProjectDir(), "/build/outputs/checkstyle/checkstyle.xml");
    }
}
