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
package com.btkelly.gnag.tasks;

import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.reporters.ViolationDetectorFactory;
import com.btkelly.gnag.reporters.ViolationResolver;
import com.btkelly.gnag.utils.ProjectHelper;

import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobbake4 on 4/1/16.
 */
public class GnagCheckTask extends BaseGnagCheckTask implements ViolationResolver {

  static final String TASK_NAME = "gnagCheck";

  public static void addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension, List<BaseGnagCheckTask> subTasks) {
    Map<String, Object> taskOptions = new HashMap<>();

    taskOptions.put(Task.TASK_NAME, TASK_NAME);
    taskOptions.put(Task.TASK_TYPE, GnagCheckTask.class);
    taskOptions.put(Task.TASK_GROUP, "Verification");
    taskOptions.put(Task.TASK_DEPENDS_ON, "check");
    taskOptions.put(Task.TASK_DESCRIPTION, "Runs Gnag checks and generates an HTML report");

    Project project = projectHelper.getProject();

    GnagCheckTask gnagCheckTask = (GnagCheckTask) project.task(taskOptions, TASK_NAME);
    gnagCheckTask.setGnagPluginExtension(gnagPluginExtension);
    gnagCheckTask.dependsOn(subTasks);

    gnagCheckTask.resolve(project);
  }

  @Override
  public void resolve(Project project) {
    violationDetectors.addAll(ViolationDetectorFactory.getAllViolationDetector(project, gnagPluginExtension));
  }
}
