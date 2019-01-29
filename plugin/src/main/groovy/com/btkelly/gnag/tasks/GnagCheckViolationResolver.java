package com.btkelly.gnag.tasks;

import org.gradle.api.Project;

public interface GnagCheckViolationResolver {
    void resolve(Project project);
}
