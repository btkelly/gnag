/**
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
package com.btkelly.gnag.reporters;

import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.utils.ProjectHelper;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

public class ViolationDetectorFactory {

    public static List<ViolationDetector> getAllViolationDetector(Project project, GnagPluginExtension gnagPluginExtension) {
        List<ViolationDetector> violationDetectors = new ArrayList<>();
        violationDetectors.add(getCheckStyleViolationDetector(project, gnagPluginExtension));
        violationDetectors.add(getPmdViolationDetector(project, gnagPluginExtension));
        violationDetectors.add(getFindBugViolationDetector(project, gnagPluginExtension));
        violationDetectors.add(getKtLintViolationDetector(project, gnagPluginExtension));
        violationDetectors.add(getDetektViolationDetector(project, gnagPluginExtension));
        violationDetectors.add(getAndroidLintViolationDetector(project, gnagPluginExtension));

        return violationDetectors;
    }

    public static ViolationDetector getCheckStyleViolationDetector(Project project, GnagPluginExtension gnagPluginExtension) {
        ProjectHelper projectHelper = new ProjectHelper(project);
        boolean isEnabled = gnagPluginExtension.checkstyle.isEnabled() && projectHelper.hasJavaSourceFiles();
        return new CheckstyleViolationDetector(project, isEnabled, gnagPluginExtension.checkstyle);
    }

    public static ViolationDetector getPmdViolationDetector(Project project, GnagPluginExtension gnagPluginExtension) {
        ProjectHelper projectHelper = new ProjectHelper(project);
        boolean isEnabled = gnagPluginExtension.pmd.isEnabled() && projectHelper.hasJavaSourceFiles();
        return new PMDViolationDetector(project, isEnabled, gnagPluginExtension.pmd);
    }

    public static ViolationDetector getFindBugViolationDetector(Project project, GnagPluginExtension gnagPluginExtension) {
        ProjectHelper projectHelper = new ProjectHelper(project);
        boolean isEnabled = gnagPluginExtension.findbugs.isEnabled() && projectHelper.hasJavaSourceFiles();
        return new FindbugsViolationDetector(project, isEnabled, gnagPluginExtension.findbugs);
    }

    public static ViolationDetector getKtLintViolationDetector(Project project, GnagPluginExtension gnagPluginExtension) {
        ProjectHelper projectHelper = new ProjectHelper(project);
        boolean isEnabled = gnagPluginExtension.ktlint.isEnabled() && projectHelper.hasKotlinSourceFiles();
        return new KtlintViolationDetector(project, isEnabled, gnagPluginExtension.ktlint);
    }

    public static ViolationDetector getDetektViolationDetector(Project project, GnagPluginExtension gnagPluginExtension) {
        ProjectHelper projectHelper = new ProjectHelper(project);
        boolean isEnabled = gnagPluginExtension.detekt.isEnabled() && projectHelper.hasKotlinSourceFiles();
        return new DetektViolationDetector(project, isEnabled, gnagPluginExtension.detekt);
    }

    public static ViolationDetector getAndroidLintViolationDetector(Project project, GnagPluginExtension gnagPluginExtension) {
        ProjectHelper projectHelper = new ProjectHelper(project);
        boolean isEnabled = projectHelper.isAndroidProject() && gnagPluginExtension.androidLint.isEnabled();
        return new AndroidLintViolationDetector(project, isEnabled, gnagPluginExtension.androidLint);
    }
}
