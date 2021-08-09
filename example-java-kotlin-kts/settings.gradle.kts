rootProject.name = "example-java-kotlin-kts"

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.btkelly") {
                useModule("com.btkelly:gnag:" + requested.version)
            }
        }
    }
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}