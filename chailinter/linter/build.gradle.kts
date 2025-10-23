plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.droidconke.chailinter.linter"
    compileSdk = 35
}

dependencies {
    implementation(project(":chailinter:rules"))
    lintPublish(project(":chailinter:rules"))
}
