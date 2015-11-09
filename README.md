# gnag <a href="https://travis-ci.org/btkelly/gnag"><img src="https://travis-ci.org/btkelly/gnag.svg" /></a>&nbsp;<a href='https://bintray.com/btkelly/maven/gnag-gradle-plugin/_latestVersion'><img src='https://api.bintray.com/packages/btkelly/maven/gnag-gradle-plugin/images/download.svg'></a>
A Gradle plugin that helps facilitate Github PR checking and automatic commenting of violations.

# Usage

The simplest way to use this plugin is in conjunction with the <a href="https://github.com/noveogroup/android-check">android-check</a> plugin. This allows you to integrate checkstyle, findbugs, and PMD with minimal effort. It is important to leave the report output paths as default and disable the `abortOnError` flag to allow gnag to finish collecting all reports.

Add the following to the build script of your project

```
buildscript {
    repositories {
        maven {
            jcenter()
        }
    }
    dependencies {
        classpath 'com.btkelly:gnag:{current version}'
    }
}

apply plugin: 'gnag-plugin'

gnag {
    gitHubRepoName 'user/repo'
    gitHubAuthToken "12312n3j12n3jk1"
    gitHubIssueNumber "11"
    failBuildOnError true
}
```

This is the simplest way to add automatic PR checking and commenting to your project. The options above can be overridden by passing command line parameters with the same name to your build. This is helpful when using in conjunction with a CI system to allow automated builds.

# License

Copyright 2015 Bryan Kelly

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
