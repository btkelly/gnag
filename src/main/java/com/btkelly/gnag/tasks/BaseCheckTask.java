/**
 * Copyright 2015 Bryan Kelly
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
import com.btkelly.gnag.reporters.CheckstyleReporter;
import com.btkelly.gnag.reporters.CommentReporter;
import com.btkelly.gnag.reporters.FindbugsReporter;
import com.btkelly.gnag.reporters.PMDReporter;
import com.btkelly.gnag.utils.Logger;
import org.gradle.api.DefaultTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobbake4 on 12/3/15.
 */
public abstract class BaseCheckTask extends DefaultTask {

    protected GnagPluginExtension getGnagPluginExtension() {
        return GnagPluginExtension.getExtension(getProject());
    }

    protected boolean failBuildOnError() {
        return getGnagPluginExtension().getFailBuildOnError();
    }

    protected ViolationComment buildViolationComment() {

        List<CommentReporter> reporters = loadReporters();

        Logger.logD("Collecting violation reports");

        StringBuilder commentBuilder = new StringBuilder();

        boolean failBuild = false;

        for (int index = 0; index < reporters.size(); index++) {

            CommentReporter githubCommentReporter = reporters.get(index);

            String violationText = githubCommentReporter.textToAppendComment(getProject());
            commentBuilder.append(violationText);

            if (githubCommentReporter.shouldFailBuild(getProject())) {
                Logger.logD(githubCommentReporter.reporterName() + " found violations");
                failBuild = true;
            }

            if (violationText.trim().length() != 0 && index != reporters.size() - 1) {
                commentBuilder.append("\n\n");
            }
        }

        return new ViolationComment(failBuild, commentBuilder.toString());
    }

    private List<CommentReporter> loadReporters() {
        Logger.logD("Loading reporters...");

        //TODO allow enable / disable reporters
        //TODO allow custom reporters to be loaded
        List<CommentReporter> reporters = new ArrayList<>();

        FindbugsReporter findbugsReporter = new FindbugsReporter();
        reporters.add(findbugsReporter);

        PMDReporter pmdReporter = new PMDReporter();
        reporters.add(pmdReporter);

        CheckstyleReporter checkstyleReporter = new CheckstyleReporter();
        reporters.add(checkstyleReporter);

        Logger.logD("Finished loading reporters");

        return reporters;
    }

}