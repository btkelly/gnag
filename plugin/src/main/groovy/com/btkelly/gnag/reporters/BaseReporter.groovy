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
import com.btkelly.gnag.utils.GnagReportBuilder
import org.gradle.api.Project

/**
 * Created by bobbake4 on 4/1/16.
 */
abstract class BaseReporter {

    abstract void executeReporter()

    abstract boolean hasErrors()

    abstract String reporterName()

    abstract File reportFile()

    abstract void appendReport(GnagReportBuilder gnagReportBuilder)

    protected final ReporterExtension reporterExtension
    protected final Project project

    BaseReporter(ReporterExtension reporterExtension, Project project) {
        this.reporterExtension = reporterExtension
        this.project = project
    }

    public boolean isEnabled() {
        return reporterExtension.enabled
    }

    protected List<File> getAndroidSources() {
        project.android.sourceSets.inject([]) {
            dirs, sourceSet -> dirs + sourceSet.java.srcDirs
        }
    }

    protected File getReportsDir() {
        File reportsDir = new File(project.buildDir.path + "/outputs/gnag/")
        reportsDir.mkdirs()
        return reportsDir
    }

    private String getDescription() {
        return "Executes all checks for the " + reporterName() + " reporter"
    }
}
