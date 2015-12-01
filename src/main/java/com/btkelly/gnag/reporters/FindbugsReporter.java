package com.btkelly.gnag.reporters;

import com.btkelly.gnag.models.findbugs.BugCollection;
import com.btkelly.gnag.models.findbugs.BugInstance;
import org.gradle.api.Project;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Created by bobbake4 on 12/1/15.
 */
public class FindbugsReporter implements CommentReporter {

    @Override
    public boolean shouldFailBuild(Project project) {
        try {
            return getFindBugsReport(project).shouldFailBuild();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String textToAppendComment(Project project) {

        //println "Parsing Findbugs violations";

        StringBuilder stringBuilder = new StringBuilder();

        try {
            BugCollection bugCollection = getFindBugsReport(project);

            if (bugCollection.shouldFailBuild()) {

                stringBuilder.append("Findbugs Violations:");
                stringBuilder.append("\\n----------------------------------\\n");

                for (BugInstance bugInstance : bugCollection.getBugInstance()) {

                    stringBuilder.append("<b>Violation: </b> " + bugInstance.getType());
                    stringBuilder.append("\\n");
                    stringBuilder.append("<b>Class: </b>" + bugInstance.getSourceLine().getClassname());
                    stringBuilder.append(" - ");
                    stringBuilder.append(" <b>Line: </b>" + bugInstance.getSourceLine().getStart());
                    stringBuilder.append("\\n");
                    stringBuilder.append(bugInstance.getShortMessage());
                    stringBuilder.append("\\n\\n");
                }
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //println "Finished parsing Findbugs violations";

        return stringBuilder.toString();
    }

    @Override
    public String reporterName() {
        return "FindBugs Reporter";
    }

    private BugCollection getFindBugsReport(Project project) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(BugCollection.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return (BugCollection) unmarshaller.unmarshal(getFindbugsReportFile(project));
    }

    private java.io.File getFindbugsReportFile(Project project) {
        return new java.io.File(project.getProjectDir(), "/build/outputs/findbugs/findbugs.xml");
    }
}
