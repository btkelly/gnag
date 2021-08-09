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

import com.btkelly.gnag.extensions.GnagPluginExtension
import com.btkelly.gnag.extensions.ReporterExtension
import com.btkelly.gnag.models.Violation
import com.btkelly.gnag.reporters.utils.CheckstyleParser
import com.btkelly.gnag.tasks.GnagCheckTask
import com.btkelly.gnag.utils.ProjectHelper
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * Created by bobbake4 on 4/1/16.
 */
class CheckstyleViolationDetector extends BaseViolationDetector {

    private final CheckstyleParser checkstyleParser = new CheckstyleParser()

    CheckstyleViolationDetector(final Project project, final ReporterExtension reporterExtension) {
        super(project, reporterExtension)
    }

    @Override
    List<Violation> getDetectedViolations() {
        return checkstyleParser.parseViolations(project, reportFile().text, name())
    }

    @Override
    String name() {
        return "Checkstyle"
    }

    @Override
    File reportFile() {
        File parentDir = getReportDir(projectHelper)
        return new File(parentDir, "main.xml")
    }

    static ViolationDetector configure(ProjectHelper projectHelper, GnagCheckTask gnagCheckTask, GnagPluginExtension gnagPluginExtension) {
        if (gnagPluginExtension.checkstyle.isEnabled() && projectHelper.hasJavaSourceFiles()) {
            String overrideToolVersion = gnagPluginExtension.checkstyle.getToolVersion()
            String toolVersion = overrideToolVersion != null ? overrideToolVersion : GnagCheckTask.CHECKSTYLE_TOOL_VERSION

            projectHelper.project.getConfigurations().create("gnagCheckstyle")
            projectHelper.project.plugins.apply("checkstyle")
            projectHelper.project.checkstyle.toolVersion = toolVersion
            projectHelper.project.checkstyle.ignoreFailures = true
            projectHelper.project.checkstyle.showViolations = false
            projectHelper.project.checkstyle.reportsDir = getReportDir(projectHelper)

            ReporterExtension reporterExtension = gnagPluginExtension.checkstyle

            if (reporterExtension.hasReporterConfig()) {
                projectHelper.project.checkstyle.configFile = reporterExtension.getReporterConfig()
            } else {
                final InputStream defaultCheckstyleConfigInputStream = ViolationDetector.getClassLoader().getResourceAsStream("checkstyle.xml")
                final File tempCheckstyleConfigFile = File.createTempFile("checkstyleConfig", null)
                tempCheckstyleConfigFile.deleteOnExit()
                FileUtils.copyInputStreamToFile(defaultCheckstyleConfigInputStream, tempCheckstyleConfigFile)
                projectHelper.project.checkstyle.configFile = tempCheckstyleConfigFile
            }

            gnagCheckTask.dependsOn("checkstyleMain")

            return new CheckstyleViolationDetector(projectHelper.project, gnagPluginExtension.checkstyle)
        }

        return null
    }

    private static File getReportDir(ProjectHelper projectHelper) {
        File parentDir = new File(projectHelper.getReportsDir(), "/checkstyle/")
        parentDir.mkdirs()
        return parentDir
    }

}
