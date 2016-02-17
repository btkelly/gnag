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

import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobbake4 on 2/15/16.
 */
public class BuildIntegrationTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    private List<File> classPaths;

    @Before
    public void setup() throws IOException {

        //Copy Plugin source files to temp directory to be checked
        FileUtils.copyDirectory(new File("src/test/resources"), testProjectDir.getRoot());

        //Add Plugin classpath to the test runner
        File testClassPath = new File("build/createClasspathManifest/plugin-classpath.txt");

        if (!testClassPath.exists()) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.");
        }

        List<String> classPathStrings = FileUtils.readLines(testClassPath, null);

        classPaths = new ArrayList<>();

        for (String classPath : classPathStrings) {
            classPaths.add(new File(classPath));
        }

        System.out.println(">>>>>>>>>>> Build Integration Test Started");
    }

    @After
    public void tearDown() {
        try {
            System.out.println(">>>>>>>>>>> Build Integration Test Complete, see results at \"build/reports/gnag-test-runs/" + testProjectDir.getRoot().getName() + "\"");
            FileUtils.copyDirectory(testProjectDir.getRoot(), new File("build/reports/gnag-test-runs/", testProjectDir.getRoot().getName()));
        } catch (IOException ignored) { }
    }

    @Test
    public void testHelloWorldTask() throws IOException {

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("checkLocal")
                .withPluginClasspath(classPaths)
                .buildAndFail();

        System.out.print(result.getOutput());
    }
}
