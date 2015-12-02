package com.btkelly.gnag.models.github;

/**
 * Created by bobbake4 on 12/2/15.
 */
public enum GitHubStatusType {

    SUCCESS("All code quality checks have passed"),
    PENDING("Checking code quality"),
    ERROR("An error was encountered while checking code quality"),
    FAILURE("Code quality checks found violations, check PR comments for details");

    private final String description;

    GitHubStatusType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
