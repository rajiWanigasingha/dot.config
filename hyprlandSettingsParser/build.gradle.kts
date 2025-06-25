plugins {
    kotlin("jvm") version "2.0.20"
    alias(libs.plugins.kotlin.plugin.serialization)
    `maven-publish`
}

group = "org.hyprconfig"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation(libs.logback.classic)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.hyprconfig"
            artifactId = "hyprConfigParser"
            version = "1.0.0"
        }
    }

    repositories {
        mavenLocal()
    }
}
