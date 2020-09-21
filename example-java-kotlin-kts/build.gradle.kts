buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("com.btkelly:gnag:2.5.0")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    id("com.github.hierynomus.license") version "0.15.0"
    application
}

apply(plugin = "com.btkelly.gnag")

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

group = "com.btkelly.gnag"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "com.btkelly.gnag.example.KotlinFileInKotlinSourceSet"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.10")
    testImplementation(group = "junit", name = "junit", version = "4.13")
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
