buildscript {
    repositories {
        mavenLocal()
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.11"
    id("com.github.hierynomus.license") version "0.15.0"
    application
    id("com.btkelly.gnag") version "2.2.0"
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
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.11")
    testCompile(group = "junit", name = "junit", version = "4.12")
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

    ktlint { isEnabled = true }

    github {
        repoName("btkelly/android-svsu-acm-20131120")
        authToken("0000000000000")
        issueNumber("1")
        setCommentInline(true)
        setCommentOnSuccess(true)
    }
}
