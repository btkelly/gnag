plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.github.hierynomus.license").version("0.15.0")
    id("com.btkelly.gnag").version("2.4.1")
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

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    testImplementation("junit:junit:4.12")
}

license {
    header = file("../../LICENSE_HEADER.txt")
    strictCheck = true
}

tasks.getByName("clean").dependsOn("licenseFormat")

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
