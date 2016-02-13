# gnag <a href="https://travis-ci.org/btkelly/gnag"><img src="https://travis-ci.org/btkelly/gnag.svg" /></a> [![Coverage Status](https://coveralls.io/repos/btkelly/gnag/badge.svg?branch=master&service=github)](https://coveralls.io/github/btkelly/gnag?branch=master) <a href="http://www.detroitlabs.com/"><img src="https://img.shields.io/badge/Sponsor-Detroit%20Labs-000000.svg" /></a> <a href='https://bintray.com/btkelly/maven/gnag-gradle-plugin/_latestVersion'><img src='https://api.bintray.com/packages/btkelly/maven/gnag-gradle-plugin/images/download.svg'></a> [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Gandalf-green.svg?style=true)](https://android-arsenal.com/details/1/3128)
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

This is the simplest way to add automatic PR checking and commenting to your project. The options defined in the gnag closure can be overridden by passing command line parameters with the same name to your build. This is helpful when using in conjunction with a CI system to allow automated builds.

# Example Output

Here is an example of the output posted to a GitHub pr on a project using gnag to enforce quality checks.

<img src="https://cloud.githubusercontent.com/assets/826036/11042826/641378e2-86e7-11e5-90ff-555a7cafd78c.png" />

# License

    Copyright 2016 Bryan Kelly
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
