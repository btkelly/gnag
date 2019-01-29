package com.btkelly.gnag.tasks;

import com.btkelly.gnag.extensions.GnagPluginExtension;
import com.btkelly.gnag.reporters.FindbugsViolationDetector;
import com.btkelly.gnag.utils.ProjectHelper;

import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.HashMap;
import java.util.Map;

public class GnagFindBugCheckTask extends BaseGnagCheckTask implements GnagCheckViolationResolver {
    private static final String TASK_NAME = "gnagCheckFindBugs";

    public static void addTask(ProjectHelper projectHelper, GnagPluginExtension gnagPluginExtension) {
        Map<String, Object> taskOptions = new HashMap<>();

        taskOptions.put(Task.TASK_NAME, TASK_NAME);
        taskOptions.put(Task.TASK_TYPE, GnagFindBugCheckTask.class);
        taskOptions.put(Task.TASK_GROUP, "Verification");
        taskOptions.put(Task.TASK_DEPENDS_ON, "check");
        taskOptions.put(Task.TASK_DESCRIPTION, "Runs FindBugs checks and generates an HTML report");

        Project project = projectHelper.getProject();

        GnagFindBugCheckTask gnagCheckTask = (GnagFindBugCheckTask) project.task(taskOptions, TASK_NAME);
        gnagCheckTask.setGnagPluginExtension(gnagPluginExtension);
        gnagCheckTask.resolve(project);
    }

    @Override
    public void resolve(Project project) {
        if (gnagPluginExtension.findbugs.isEnabled() && projectHelper.hasJavaSourceFiles()) {
            violationDetectors.add(new FindbugsViolationDetector(project, gnagPluginExtension.findbugs));
        }
    }
}
