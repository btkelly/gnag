# Issue Tracking

Library issues are tracked using [GitHub Issues](https://github.com/btkelly/gnag/issues). Please review all existing issues before creating a new one. If you would like to work on an open issue, please comment to that effect and assign yourself to the issue.

# Common Tasks

## Testing changes to the plugin

### `gnagCheck`

Executing the supplied script

```shell
./buildAndRunExample.sh
```

will

- publish a Gnag artifact based on the code currently in your working directory to your local Maven repository;
- run the `gnagCheck` task defined by this artifact on the example Android application project.

Assuming the artifact was successfully built, published, and run, you should find a local Gnag report at `example/app/build/outputs/gnag/gnag.html`. The example Android application deliberately includes suspicious code that Gnag will flag as violations.

### `gnagReport`

Testing commenting on pull requests is a little more involved.

#### Testing aggregated pull request comments

1. Create your own GitHub repository for Gnag testing.
2. Generate a new auth token for your GitHub account. Make sure it has all repository permissions.
3. Open a new pull request within your test repository.
4. In the `example/app/build.gradle` file, update the `repoName`, `authToken` and `issueNumber` entries in the `github` block within the `gnag` configuration closure as follows:

    ```groovy
    github {
        repoName 'your_github_id/test_repository_name'
        authToken 'generated_auth_token'
        issueNumber 'test_repository_pull_request_number'
    }
    ```

5. Update `./buildAndRunExample.sh` to run `./gradlew clean gnagReport` rather than `./gradlew clean gnagCheck`.

Executing

```shell
./buildAndRunExample.sh
```

will now

- publish a Gnag artifact based on the code currently in your working directory to your local Maven repository;
- run the `gnagReport` task defined by this artifact on the example Android application project.

Assuming the artifact was successfully built, published, and run, you should see Gnag post an aggregated violations comment on the pull request you opened in your test repository.

#### Testing inline pull request comments

Perform all the steps from the [Testing aggregated comments](#testing-aggregated-comments) section, then in addition:

1. Create a new branch in your test repository.
2. Copy the contents of the example application module from the Gnag repository to this branch. Make sure that the `app` folder is in the root of your repository.
3. Open a pull request from this branch into master.
4. In the `example/app/build.gradle` file, update the `issueNumber` entry in the `github` block within the `gnag` configuration closure to match the issue number of this new pull request.

Executing

```shell
./buildAndRunExample.sh
```

should now post inline pull request comments (where appropriate).

# Inline Licenses

Before opening a pull request, you must generate license headers in any new source files by executing the Gradle command:

```shell
./gradlew licenseFormat
```

The Travis CI pull request build will fail if any source file is missing this generated header.
