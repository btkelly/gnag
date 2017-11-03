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
package com.btkelly.gnag.utils

import org.gradle.api.Project

/**
 * Created by bobbake4 on 4/19/16.
 */
class ProjectHelper {

    private final Project project

    ProjectHelper(Project project) {
        this.project = project
    }

    public Project getProject() {
        return project
    }

    public boolean isAndroidProject() {
        return project.getExtensions().findByName("android") != null;
    }

    public List<File> getJavaSourceFiles() {
        return getSourceFilesWithSuffices(Collections.singletonList(".java"))
    }

    public List<File> getKotlinSourceFiles() {
        return getSourceFilesWithSuffices(Arrays.asList(".kt", ".kts"))
    }

    public File getReportsDir() {
        File reportsDir = new File(project.buildDir.path + "/outputs/gnag/")
        reportsDir.mkdirs()
        return reportsDir
    }

    public File getKtlintReportFile() {
        return new File(getReportsDir(), "ktlint_report.xml")
    }

    private Collection<File> getSourceFilesWithSuffices(final List<String> suffices) {
        final Collection<File> allSourceFiles

        if (isAndroidProject()) {
            allSourceFiles = project.android.sourceSets.inject([]) {
                dirs, sourceSet -> dirs + sourceSet.allSource
            }
        } else {
            allSourceFiles = project.sourceSets.inject([]) {
                dirs, sourceSet -> dirs + sourceSet.allSource
            }
        }

        return allSourceFiles.findAll { File file ->
            suffices.any { suffix ->
                file.name.endsWith(suffix)
            }
        }
    }

}
