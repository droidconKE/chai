// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.compose) apply false
}

// Apply performance optimizations to all subprojects
subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.isIncremental = true
    }

    // Skip unnecessary tasks in CI
    //See if i can add this to build Logic
    tasks.matching {
        it.name.contains("lint", ignoreCase = true) &&
                it.name.contains("Baseline", ignoreCase = true)
    }.configureEach {
        enabled = System.getenv("CI") != "true"
    }
}
