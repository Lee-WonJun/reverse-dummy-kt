import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    application
}

group = "reverse.dummy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", "1.9.24"))
    implementation(kotlin("reflect", "1.9.24"))
    implementation(kotlin("stdlib-jdk8", "1.9.24"))
    implementation(kotlin("stdlib-jdk7", "1.9.24"))
    implementation(kotlin("script-runtime", "1.9.24"))

    val version = "5.3.1"
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$version")
    testImplementation("io.kotest:kotest-runner-junit5:$version")
    testImplementation("io.kotest:kotest-assertions-core:$version")
    testImplementation("io.kotest:kotest-property:$version")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//21
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}