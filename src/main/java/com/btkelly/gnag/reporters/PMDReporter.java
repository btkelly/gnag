package com.btkelly.gnag.reporters;

import com.btkelly.gnag.models.pmd.File;
import com.btkelly.gnag.models.pmd.Pmd;
import com.btkelly.gnag.models.pmd.Violation;
import org.gradle.api.Project;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Comment reporter for PMD. Looks for PMD report in the default location "/build/outputs/pmd/pmd.xml"
 */
public class PMDReporter implements CommentReporter {

    /**
     * Looks through the PMD report and determines if the root has any children.
     * @param project - current project being built
     * @return - true if the root node has children
     */
    @Override
    public boolean shouldFailBuild(Project project) {
        try {
            return getPmdReport(project).shouldFailBuild();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Loops through all PMD violations and pulls out file name, help url, line number, error message, and rule
     * @param project - current project being built
     * @return - return text to append to current comment
     */
    @Override
    public String textToAppendComment(Project project) {

        //println "Parsing PMD violations";

        StringBuilder stringBuilder = new StringBuilder();

        try {
            Pmd pmd = getPmdReport(project);

            if (pmd.shouldFailBuild()) {

                stringBuilder.append("PMD Violations:");
                stringBuilder.append("\n----------------------------------\n");

                String projectDir = project.getProjectDir().toString();

                for (File file : pmd.getFile()) {

                    String fileName = file.getName();
                    fileName = fileName.replace(projectDir, "");
                    fileName = fileName.replace("/src/main/java/", "");
                    fileName = fileName.replace("/", ".");

                    for (Violation violation : file.getViolation()) {

                        stringBuilder.append("<b>Violation: </b> " + violation.getRule());
                        stringBuilder.append("\n");
                        stringBuilder.append("<b>Help: </b> " + violation.getExternalInfoUrl());
                        stringBuilder.append("\n");
                        stringBuilder.append("<b>Class: </b>" + fileName);
                        stringBuilder.append(" - ");
                        stringBuilder.append(" <b>Line: </b>" + violation.getBeginline());
                        stringBuilder.append(violation.getValue());
                        stringBuilder.append("\n\n");

                    }
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //println "Finished parsing PMD violations";

        return stringBuilder.toString();
    }

    @Override
    public String reporterName() {
        return "PMD Reporter";
    }

    private Pmd getPmdReport(Project project) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Pmd.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return (Pmd) unmarshaller.unmarshal(getPMDReportFile(project));
    }

    private java.io.File getPMDReportFile(Project project) {
        return new java.io.File(project.getProjectDir(), "/build/outputs/pmd/pmd.xml");
    }
}
