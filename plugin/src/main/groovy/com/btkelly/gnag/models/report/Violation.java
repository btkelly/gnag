package com.btkelly.gnag.models.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Violation {
    
    @NotNull
    private final String name;
    
    @NotNull
    private final String reporterName;

    @Nullable
    private final String comment;

    @Nullable
    private final String url;

    public Violation(
            @NotNull  final String name,
            @NotNull  final String reporterName,
            @Nullable final String comment,
            @Nullable final String url) {
        
        this.name = name;
        this.reporterName = reporterName;
        this.comment = comment;
        this.url = url;
    }
    
}
