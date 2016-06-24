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
import edu.umd.cs.findbugs.anttask.FindBugsTask
import groovy.util.slurpersupport.GPathResult
import org.apache.tools.ant.types.FileSet
import org.apache.tools.ant.types.Path
import org.gradle.api.Project

import java.util.stream.Collectors

import static com.btkelly.gnag.utils.StringUtils.sanitizePreservingNulls
import static com.btkelly.gnag.utils.StringUtils.sanitizeToNonNull
/**
 * Created by bobbake4 on 4/1/16.
 */
class FindbugsViolationDetector extends BaseExecutedViolationDetector {

    FindbugsViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
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
        final List<String> sourceFilePaths = computeSourceFilePaths(xml)

        final List<Violation> result = new ArrayList<>()

        xml.BugInstance.list()
            .each { violation ->
                final String violationName = sanitizeToNonNull((String) violation.@type.text())

                final String relativeFilePath =
                        computeRelativeFilePathIfPossible((GPathResult) violation, sourceFilePaths)
            
                final String lineNumberString = sanitizeToNonNull((String) violation.SourceLine.@end.text())
                final Integer lineNumber = computeLineNumberFromString(lineNumberString, violationName)

                result.add(new Violation(
                        violationName,
                        sanitizeToNonNull((String) name()),
                        sanitizePreservingNulls((String) violation.ShortMessage.text()),
                        null,
                        relativeFilePath,
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

    private static List computeSourceFilePaths(final GPathResult xml) {
        final List<String> result = new ArrayList<>()

        xml.Project.SrcDir.list().each { sourceFile ->
            result.add((String) sanitizePreservingNulls((String) sourceFile.text()))
        }

        return result
    }

    private String computeRelativeFilePathIfPossible(final GPathResult violation, final List<String> sourceFilePaths) {
        final String shortFilePath = sanitizeToNonNull((String) violation.SourceLine.@sourcepath)

        final List<String> longFilePaths =
                sourceFilePaths
                        .stream()
                        .filter { it != null && it.endsWith(shortFilePath) }
                        .collect(Collectors.toList())

        if (longFilePaths.isEmpty() || longFilePaths.size() > 1) {
            return null
        } else {
            return computeFilePathRelativeToProjectRoot(longFilePaths.get(0))
        }
    }

}
