buildscript {
    repositories {
        mavenLocal()
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    id("com.github.hierynomus.license") version "0.15.0"
    application
    id("com.btkelly.gnag") version "2.4.1"
}

group = "com.btkelly.gnag"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "com.btkelly.gnag.example.KotlinFileInKotlinSourceSet"
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.11")
    testImplementation(group = "junit", name = "junit", version = "4.12")
}

license {
    header = file("../LICENSE_HEADER.txt")
    strictCheck = true
}

tasks.clean {
    dependsOn(tasks.licenseFormat)
}

gnag {
    isEnabled = true
    setFailOnError(true)

    github {
        repoName("btkelly/android-svsu-acm-20131120")
        authToken("0000000000000")
        issueNumber("1")
        setCommentInline(true)
        setCommentOnSuccess(true)
    }
}
