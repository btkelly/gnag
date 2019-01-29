package com.btkelly.gnag.tasks;

import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.reporters.CheckstyleViolationDetector;
import com.btkelly.gnag.utils.ProjectHelper;

import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.HashMap;
import java.util.Map;

public class GnagCStyleCheckTask extends BaseGnagCheckTask implements GnagCheckViolationResolver {
    private static final String TASK_NAME = "gnagCheckCStyle";

    public static void addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(Task.TASK_TYPE, GnagCStyleCheckTask.class);
        taskOptions.put(Task.TASK_GROUP, "Verification");
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs Check Style checks and generates an HTML report");

        Project project = projectHelper.getProject();

        GnagCStyleCheckTask gnagCheckTask = (GnagCStyleCheckTask) project.task(taskOptions, TASK_NAME);
        gnagCheckTask.setGnagPluginExtension(gnagPluginExtension);
        gnagCheckTask.resolve(project);
    }

    @Override
    public void resolve(Project project) {
        if (gnagPluginExtension.checkstyle.isEnabled() && projectHelper.hasJavaSourceFiles()) {
            violationDetectors.add(new CheckstyleViolationDetector(project, gnagPluginExtension.checkstyle));
        }
    }
}
