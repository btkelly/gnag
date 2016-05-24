package com.btkelly.gnag.models.report;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class File {
    
    @NotNull
    private final String relativePath;

    /**
     * A mapping between file line numbers and the lists of violations detected in those lines.
     */
    private final Map<Integer, List<Violation>> indexedViolations = new HashMap<>();

    public File(@NotNull final String relativePath) {
        this.relativePath = relativePath;
    }
    
}
