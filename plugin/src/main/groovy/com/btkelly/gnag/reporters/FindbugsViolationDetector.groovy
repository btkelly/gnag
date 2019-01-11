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
import com.btkelly.gnag.reporters.utils.LineNumberParser
import com.btkelly.gnag.reporters.utils.PathCalculator
import edu.umd.cs.findbugs.anttask.FindBugsTask
import groovy.util.slurpersupport.GPathResult
import org.apache.commons.io.FilenameUtils
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
        Set<String> sourceSetRootDirPaths = projectHelper.getSourceSetRootDirPaths()

        projectHelper.getJavaSourceFiles()
                .each { File sourceFile ->
                    FileSet sourcePathFileSet = new FileSet()
                    sourcePathFileSet.file = sourceFile
                    sourcePath.addFileset(sourcePathFileSet)

                    FileSet antPathFileSet = new FileSet()
                    antPathFileSet.file = sourceFile

                    Path antPath = (Path) project.ant.path()
                    antPath.addFileset(antPathFileSet)

                    /*
                     * Compute the path to the source set directory that contains sourceFile. (This code assumes there
                     * will be at least one match for every source file. Can there ever be more than one?).
                     */
                    String containingSourceSetRootDirPath = sourceSetRootDirPaths
                            .findAll { String sourceSetRootDirPath -> sourceFile.absolutePath.startsWith(sourceSetRootDirPath) }
                            .first()

                    /*
                     * Strip the path to the containing source set directory from the front of the path to sourceFile,
                     * and remove the trailing .java since we will use this pattern to locate .class files.
                     */
                    String relativePathToClass = sourceFile
                            .absolutePath
                            .replaceAll(containingSourceSetRootDirPath, '')
                            .replaceAll(".java\$", '')

                    // Note: retrieving via project.ant.fileset() works, but constructing a brand new Fileset does not.
                    FileSet taskFileSet = project.ant.fileset() as FileSet

                    taskFileSet.dir = project.buildDir
                    taskFileSet.setIncludes("**$relativePathToClass*")
                    findBugsTask.addFileset(taskFileSet)
                }

        Path classpath = findBugsTask.createClasspath()

        project.rootProject.buildscript.configurations.classpath.resolve().each { File classpathFile ->
            classpath.createPathElement().location = classpathFile
        }

        project.buildscript.configurations.classpath.resolve().each { File classpathFile ->
            classpath.createPathElement().location = classpathFile
        }

        findBugsTask.perform()
    }

    @Override
    List<Violation> getDetectedViolations() {
        GPathResult xml = new XmlSlurper().parseText(reportFile().text)
        final List<String> sourceFilePaths = computeSourceFilePaths(xml)

        final List<Violation> result = new ArrayList<>()

        xml.BugInstance.list()
            .each { violation ->
                final String violationType = sanitizeToNonNull((String) violation.@type.text())

                final String relativeFilePath =
                        computeRelativeFilePathIfPossible((GPathResult) violation, sourceFilePaths)

                final String lineNumberString = sanitizeToNonNull((String) violation.SourceLine.@end.text())
                final Integer lineNumber = LineNumberParser.parseLineNumberString(
                        lineNumberString,
                        name(),
                        violationType,
                        project.getLogger())

                result.add(new Violation(
                        violationType,
                        name(),
                        sanitizePreservingNulls((String) violation.ShortMessage.text()),
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
        return new File(projectHelper.getReportsDir(), "findbugs.xml")
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
            return PathCalculator.calculatePathWithinProject(project, longFilePaths.get(0))
        }
    }

}
