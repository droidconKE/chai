plugins {
    `java-library`
    kotlin("jvm")
    id("com.android.lint")
}


java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
