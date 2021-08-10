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

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject

/**
 * Created by bobbake4 on 4/19/16.
 */
class ProjectHelper {

    private final Project project

    ProjectHelper(Project project) {
        this.project = project
    }

    DefaultProject getProject() {
        return project
    }

    boolean isAndroidProject() {
        return project.getExtensions().findByName("android") != null
    }

    public List<File> getJavaSourceFiles() {
        return getSourceFilesWithSuffices(["java"] as String[])
    }

    boolean hasJavaSourceFiles() {
        return !getJavaSourceFiles().isEmpty()
    }

    List<File> getKotlinSourceFiles() {
        return getSourceFilesWithSuffices(["kt", "kts"] as String[])
    }

    boolean hasKotlinSourceFiles() {
        return !getKotlinSourceFiles().isEmpty()
    }

    File getReportsDir() {
        File reportsDir = new File(project.buildDir.path + "/outputs/gnag/")
        reportsDir.mkdirs()
        return reportsDir
    }

    File getKtlintReportFile() {
        return new File(getReportsDir(), "ktlint_report.xml")
    }

    String getDetektReportFileName() {
        return "detekt_report"
    }

    private Collection<File> getSourceFilesWithSuffices(final String[] suffices) {
        Collection<File> allSourceFiles

        if (isAndroidProject()) {
            allSourceFiles = project.android.sourceSets.inject([]) { files, sourceSet ->
                sourceSet.java.srcDirs.each { File javaSrcDir ->
                    if (javaSrcDir.exists()) {
                        files = files + FileUtils.listFiles(javaSrcDir, suffices, true)
                    }
                }

                files
            }
        } else {
            allSourceFiles = project.sourceSets.inject([]) { files, sourceSet ->
                files + sourceSet.allSource
            }
        }

        return allSourceFiles.findAll { File file ->
            file.exists() && suffices.any { suffix ->
                file.name.endsWith(suffix)
            }
        }
    }

    Set<String> getSourceSetRootDirPaths() {
        final Set<File> allSourceSetRootDirs

        if (isAndroidProject()) {
            allSourceSetRootDirs = project.android.sourceSets.inject([]) {
                dirs, sourceSet -> dirs + sourceSet.java.srcDirs
            }
        } else {
            allSourceSetRootDirs = project.sourceSets.inject([]) {
                dirs, sourceSet -> dirs + sourceSet.allSource.srcDirs
            }
        }

        return allSourceSetRootDirs
                .findAll { File sourceSetRootDir -> sourceSetRootDir.exists() }
                .collect { File sourceSetRootDir -> sourceSetRootDir.absolutePath }
    }

}
