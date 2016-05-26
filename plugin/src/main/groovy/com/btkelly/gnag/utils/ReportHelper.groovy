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
class ReportHelper {

    private final Project project;

    ReportHelper(Project project) {
        this.project = project
    }

    // todo: this doesn't seem like it belongs in the ReportHelper. refactor?
    public List<File> getAndroidSources() {
        project.android.sourceSets.inject([]) {
            dirs, sourceSet -> dirs + sourceSet.java.srcDirs
        }
    }

    public File getReportsDir() {
        File reportsDir = new File(project.buildDir.path + "/outputs/gnag/")
        reportsDir.mkdirs()
        return reportsDir
    }
}
