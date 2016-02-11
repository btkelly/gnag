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

import com.btkelly.gnag.GnagPluginExtension;
import com.btkelly.gnag.models.ViolationComment;
import com.btkelly.gnag.reporters.AndroidLintReporter;
import com.btkelly.gnag.reporters.CheckstyleReporter;
import com.btkelly.gnag.reporters.CommentReporter;
import com.btkelly.gnag.reporters.FindbugsReporter;
import com.btkelly.gnag.reporters.PMDReporter;
import com.btkelly.gnag.utils.Logger;
import org.gradle.api.DefaultTask;

import java.util.ArrayList;
import java.util.List;

abstract class BaseCheckTask extends DefaultTask {

    GnagPluginExtension getGnagPluginExtension() {
        return GnagPluginExtension.getExtension(getProject());
    }

    boolean failBuildOnError() {
        return getGnagPluginExtension().getFailBuildOnError();
    }

    ViolationComment buildViolationComment() {

        List<CommentReporter> reporters = loadReporters();

        Logger.logInfo("Collecting violation reports");

        StringBuilder commentBuilder = new StringBuilder();

        boolean failBuild = false;

        for (int index = 0; index < reporters.size(); index++) {

            CommentReporter githubCommentReporter = reporters.get(index);

            if (githubCommentReporter.reporterEnabled(getProject())) {

                String violationText = githubCommentReporter.textToAppendComment(getProject());
                commentBuilder.append(violationText);

                if (githubCommentReporter.shouldFailBuild(getProject())) {
                    Logger.logInfo(githubCommentReporter.reporterName() + " found violations");
                    failBuild = true;
                }

                if (violationText.trim().length() != 0 && index != reporters.size() - 1) {
                    commentBuilder.append("\n\n");
                }
            }
        }

        return new ViolationComment(failBuild, commentBuilder.toString());
    }

    private List<CommentReporter> loadReporters() {
        Logger.logInfo("Loading reporters...");

        //TODO allow enable / disable reporters
        //TODO allow custom reporters to be loaded
        List<CommentReporter> reporters = new ArrayList<>();

        FindbugsReporter findbugsReporter = new FindbugsReporter();
        reporters.add(findbugsReporter);

        PMDReporter pmdReporter = new PMDReporter();
        reporters.add(pmdReporter);

        CheckstyleReporter checkstyleReporter = new CheckstyleReporter();
        reporters.add(checkstyleReporter);

        AndroidLintReporter androidLintReporter = new AndroidLintReporter();
        reporters.add(androidLintReporter);

        Logger.logInfo("Finished loading reporters");

        return reporters;
    }

}
