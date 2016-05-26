/*
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.btkelly.gnag.reporters

import com.btkelly.gnag.extensions.ReporterExtension
import com.btkelly.gnag.models.Violation
import com.btkelly.gnag.utils.StringUtils
import edu.umd.cs.findbugs.anttask.FindBugsTask
import groovy.util.slurpersupport.GPathResult
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.Path
import org.gradle.api.Project
/**
 * Created by bobbake4 on 4/1/16.
 */
class FindbugsViolationDetector extends BaseExecutedViolationDetector {

    FindbugsViolationDetector(ReporterExtension reporterExtension, Project project) {
        super(reporterExtension, project)
    }

    @Override
    void executeReporter() {

        FindBugsTask findBugsTask = new FindBugsTask()

        findBugsTask.project = project.ant.antProject
        findBugsTask.workHard = true
        findBugsTask.output = "xml:withMessages"
        findBugsTask.outputFile = reportFile()
        findBugsTask.failOnError = false

        if (reporterExtension.hasReporterConfig()) {
            findBugsTask.excludeFilter = reporterExtension.getReporterConfig()
        } else {
            findBugsTask.excludeFilter = new File(getClass().getClassLoader().getResource("findbugs.xml").getFile())
        }

        Path sourcePath = findBugsTask.createSourcePath()
        reportHelper.getAndroidSources().findAll { it.exists() }.each {
            sourcePath.addFileset(project.ant.fileset(dir: it))
        }

        Path classpath = findBugsTask.createClasspath()
        project.rootProject.buildscript.configurations.classpath.resolve().each {
            classpath.createPathElement().location = it
        }
        project.buildscript.configurations.classpath.resolve().each {
            classpath.createPathElement().location = it
        }

        Set<String> includes = []
        reportHelper.getAndroidSources().findAll { it.exists() }.each { File directory ->
            FileSet fileSet = project.ant.fileset(dir: directory)
            Path path = project.ant.path()
            path.addFileset(fileSet)

            path.each {
                String includePath = new File(it.toString()).absolutePath - directory.absolutePath
                includes.add("**${includePath.replaceAll('\\.java$', '')}*")
            }
        }

        findBugsTask.addFileset(project.ant.fileset(dir: project.buildDir, includes: includes.join(',')))

        findBugsTask.perform()
    }

    @Override
    List<Violation> getDetectedViolations() {
        GPathResult xml = new XmlSlurper().parseText(reportFile().text)

        final List<Violation> result = new ArrayList<>()

        xml.BugInstance.list()
                .each { violation ->
                        final Integer lineNumber;
        
                        try {
                            lineNumber = violation.SourceLine.@end.toInteger()
                        } catch (final NumberFormatException e) {
                            System.out.println("Error reading line number from Findbugs violations.");
                            e.printStackTrace();
                            lineNumber = null
                        }

                        result.add(new Violation(
                                StringUtils.sanitize((String) violation.@type.text()),
                                StringUtils.sanitize((String) name()),
                                StringUtils.sanitize((String) violation.ShortMessage.text()),
                                null,
                                StringUtils.sanitize((String) violation.SourceLine.@sourcepath), // todo: check relativity
                                lineNumber))
                }
        
        return result
    }

    @Override
    String name() {
        return "Findbugs"
    }

    @Override
    File reportFile() {
        return new File(reportHelper.getReportsDir(), "findbugs.xml")
    }
    
}
