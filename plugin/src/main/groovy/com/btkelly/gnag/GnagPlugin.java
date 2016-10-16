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
package com.btkelly.gnag;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.LibraryPlugin;
import com.android.build.gradle.api.BaseVariant;
import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.tasks.GnagCheckTask;
import com.btkelly.gnag.tasks.GnagReportTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        GnagPluginExtension gnagPluginExtension = GnagPluginExtension.loadExtension(project);

        project.afterEvaluate(evaluatedProject -> {
            if (evaluatedProject.getPlugins().hasPlugin(AppPlugin.class)) {
                AppExtension android = (AppExtension) evaluatedProject.getExtensions().getByName("android");

                android.getApplicationVariants().forEach(
                        variant -> configureTasks(evaluatedProject, gnagPluginExtension, variant));
            } else if (evaluatedProject.getPlugins().hasPlugin(LibraryPlugin.class)) {
                LibraryExtension android = (LibraryExtension) evaluatedProject.getExtensions().getByName("android");

                android.getLibraryVariants().forEach(
                        variant -> configureTasks(evaluatedProject, gnagPluginExtension, variant));
            } else {
                // fixme: throw a loud exception here
            }
        });
    }

    private void configureTasks(
            @NotNull final Project project,
            @NotNull final GnagPluginExtension gnagPluginExtension,
            @NotNull final BaseVariant variant) {
        
        GnagCheckTask.addToProject(project, gnagPluginExtension, variant);
        GnagReportTask.addToProject(project, gnagPluginExtension.github, variant);
    }
    
}
