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
