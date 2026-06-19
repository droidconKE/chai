pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    // Resolves Java toolchains (compile jvmToolchain + the Gradle daemon JVM) from foojay/Adoptium.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = ("chai")
//include(":chai")
include(":chaidemo")
include(":chailinter")
include(":chai")
