package com.btkelly.gnag.tasks;

import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.reporters.AndroidLintViolationDetector;
import com.btkelly.gnag.utils.ProjectHelper;

import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.HashMap;
import java.util.Map;

public class GnagAndroidLintCheckTask extends BaseGnagCheckTask implements GnagCheckViolationResolver {
    private static final String TASK_NAME = "gnagCheckAndroidLint";

    public static void addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(Task.TASK_TYPE, GnagAndroidLintCheckTask.class);
        taskOptions.put(Task.TASK_GROUP, "Verification");
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs Android Lint checks and generates an HTML report");

        Project project = projectHelper.getProject();

        GnagAndroidLintCheckTask gnagCheckTask = (GnagAndroidLintCheckTask) project.task(taskOptions, TASK_NAME);
        gnagCheckTask.setGnagPluginExtension(gnagPluginExtension);
        gnagCheckTask.resolve(project);
    }

    @Override
    public void resolve(Project project) {
        if (projectHelper.isAndroidProject() && gnagPluginExtension.androidLint.isEnabled()) {
            violationDetectors.add(new AndroidLintViolationDetector(project, gnagPluginExtension.androidLint));
        }
    }
}
