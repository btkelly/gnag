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
import com.btkelly.gnag.utils.ProjectHelper
import org.gradle.api.Project

/**
 * Created by bobbake4 on 4/1/16.
 */
abstract class BaseExecutedViolationDetector extends BaseViolationDetector {

    abstract void executeReporter()

    protected final ReporterExtension reporterExtension
    protected final ProjectHelper projectHelper

    BaseExecutedViolationDetector(final Project project, final boolean isEnabled, final ReporterExtension reporterExtension) {
        super(project, isEnabled)
        this.reporterExtension = reporterExtension
        this.projectHelper = new ProjectHelper(project)
    }

}
