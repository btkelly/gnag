buildscript {
    repositories {
        mavenLocal()
        jcenter()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        google()
    }
}
