plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "org.dot.config"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback.classic)
    implementation(libs.dataframe)
    implementation(libs.ktor.server.websockets)

    implementation(project(":paths"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}