buildscript {
    repositories {
        mavenLocal()
        jcenter()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.btkelly:gnag:2.5.0")
    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        google()
    }
}
