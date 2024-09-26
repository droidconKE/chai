plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.droidconke.chailinter.linter"
}

dependencies {
    implementation(project(":chailinter:rules"))
    lintPublish(project(":chailinter:rules"))
}
