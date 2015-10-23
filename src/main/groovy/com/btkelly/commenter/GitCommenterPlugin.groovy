package com.btkelly.commenter

import org.gradle.StartParameter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

/**
 * Created by bobbake4 on 10/23/15.
 */
class GitCommenterPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        Gradle gradle = project.getGradle();

        StartParameter startParameter = gradle.getStartParameter();

        if (startParameter.isOffline()) {
            println "Build is running offline. Reports will not be collected";
        } else {

            project.task("checkAndReport") << {

                println "Collecting violation reports";

                String pmdViolations = parsePMDFile(project.getProjectDir().toString());
                String commentBody = "{ \"body\" : \"" + pmdViolations + "\" }";

                println commentBody;

                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.github.com/repos/stkent/amplify/issues/11/comments").openConnection();
                httpURLConnection.setRequestProperty("Authorization", "token 77d3c796ad3f612b188bb4bbbfe08390ba0e28b6");
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                println "Sending violation reports";

                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(commentBody);
                dataOutputStream.flush();
                dataOutputStream.close();

                int statusCode = httpURLConnection.getResponseCode();

                if (statusCode >= 200 && statusCode < 300) {
                    println "Violation reports sent";
                } else {
                    println "Error sending violation reports, status code: " + statusCode + " " + httpURLConnection.getResponseMessage();
                }
            }
        }
    }

    private String parsePMDFile(String projectDir) {

        println "Parsing PMD violations";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PMD Violations:")

        def pmdFileParser = (new XmlParser()).parse(projectDir + "/pmd.xml");

        pmdFileParser.file.each { file ->

            println projectDir;

            String helpURL = cleanseXMLString(file.violation.@externalInfoUrl);
            String lineNumber = cleanseXMLString(file.violation.@beginline);
            String violationText = cleanseXMLString(file.violation.text());
            String violationRule = cleanseXMLString(file.violation.@rule);

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

        println stringBuilder.toString();

        return stringBuilder.toString();
    }

    private String cleanseXMLString(String cleanseMe) {
        return cleanseMe.replaceAll("\\[", "")
                .replaceAll("\\]","")
                .replaceAll("\n", "");
    }
}