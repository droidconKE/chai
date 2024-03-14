/**
 * Copyright 2023 droidcoke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    `java-library`
    kotlin("jvm")
    id("com.android.lint")
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.named<Test>("test") {
    useJUnit()
}

lint {
    checkAllWarnings = true
    warningsAsErrors = true
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.lint.api)

    testImplementation(libs.lint.api)
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.android.tools.lint:lint-tests:31.2.2")
}
