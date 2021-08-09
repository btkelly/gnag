plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    id("com.btkelly.gnag") version "2.6.0"
    application
}

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.31")
    testImplementation(group = "junit", name = "junit", version = "4.13")
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
