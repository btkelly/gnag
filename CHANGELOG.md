# Change Log

## v1.3.1

_03-03-2017_

- Updated Gradle versions and increased heap size
- Fixed lint report failure on newer Gradle versions

## v1.3.0

_02-07-2017_

- Added support for standard Java projects
- Fixed crash when no PMD config file was supplied
- Changed GitHub api calls to fail the build if they are not configured correctly

## v1.2.3

_11-29-2016_

- Added current project name to the GitHub status call allowing different status checks for multi module projects

## v1.2.2

_11-02-2016_

- Added the ability to provide a custom GitHub root url to support enterprise installs

## v1.2.1

_06-30-2016_

- Updated GitHubDiffParser to latest version which includes a critical bugfix.

## v1.2

_06-29-2016_

- Added commit sha1 to successful GitHub comment
- Random code cleanup

## v1.1

_06-10-2016_

- Updated dependencies to latest versions
- Changed remote comments to allow referencing specific lines in the diff

## v1.0

_04-28-2016_

Gnag initial 1.0 release!

- Includes reports for checkstyle, pmd, findbugs, and the Android linter
- All reporters are included in the plugin and can now be used with a simple apply plugin line
