buildscript {
    repositories {
        mavenLocal()
        jcenter()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        google()
    }
}
