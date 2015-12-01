/**
 * Copyright 2015 Bryan Kelly
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
package com.btkelly.gnag;

import com.btkelly.gnag.tasks.CheckAndReportTask;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.StartParameter;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.invocation.Gradle;

import java.util.HashMap;
import java.util.Map;

/**
 * The main plugin class allowing hooks into the build system. You can use this plugin by adding the following to your
 * build script.
 *
 * buildscript {
 *  repositories {
 *      maven {
 *          jcenter()
 *      }
 *  }
 *  dependencies {
 *      classpath 'com.btkelly:gnag:0.0.4'
 *  }
 * }
 *
 * apply plugin: 'gnag-plugin'
 *
 * This will reports violations of checkstyle, findbugs, and PMD. For easy setup I recommend using com.noveogroup.android.check
 * plugin with this plugin.
 *
 * You can configure the plugin inside the build script or by passing command line properties of the same name.
 *
 * gnag {
 *     gitHubRepoName "user/repo"
 *     gitHubAuthToken "12312n3j12n3jk1"
 *     gitHubIssueNumber "11"
 *     failBuildOnError true
 * }
 *
 */
public class GnagPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        Gradle gradle = project.getGradle();

        StartParameter startParameter = gradle.getStartParameter();

        if (startParameter.isOffline()) {
            ////println "Build is running offline. Reports will not be collected";
        } else {
            GnagPluginExtension.loadExtension(project);
            CheckAndReportTask.addTask(project);
        }
    }
}
