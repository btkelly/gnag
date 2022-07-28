plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.btkelly.gnag") version "3.0.2"
}

apply {
    plugin("kotlin-android")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.gnag.example"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.3.1")
    testImplementation("junit:junit:4.13.2")
}

gnag {
    isEnabled = true
    setFailOnError(true)

    androidLint {
        severity("Warning")
    }

    github {
        repoName("btkelly/android-svsu-acm-20131120")
        authToken("0000000000000")
        issueNumber("1")
        setCommentInline(true)
        setCommentOnSuccess(true)
    }
}