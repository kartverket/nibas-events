import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Versjonering settes i gradle/libs.versions.toml - vær obs. på at
// intellij ikke plukker opp på endringer der, før gradle er reloadet.
// Mer info: https://docs.gradle.org/current/userguide/platforms.html

plugins {
    alias(libs.plugins.spring.framework)
    alias(libs.plugins.spring.dependency.management)
    kotlin("jvm").version(libs.versions.kotlinPluginVersion)
    kotlin("plugin.spring").version(libs.versions.kotlinPluginVersion)
    alias(libs.plugins.flyway)
}

group = "no.kartverket"
version = "0.0.1-SNAPSHOT"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

buildscript {
    dependencies {
        classpath(libs.flyway.postgres)
    }
}

dependencies {
    implementation(libs.spring.web)
    implementation(libs.spring.jdbc)
    implementation(libs.spring.security) // Web-security
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.stdlib.jdk)
    implementation(libs.spring.actuator)
    implementation(libs.spring.validator)

    implementation(libs.springdoc.openapi.common)
    implementation(libs.springdoc.openapi.webmvc.api)
    implementation(libs.springdoc.openapi.webmvc.ui)

    implementation(libs.logstash.logback.encoder)
    implementation(libs.micrometer.registry.prometheus)

    runtimeOnly(libs.flyway.core)
    runtimeOnly(libs.flyway.postgres)
    runtimeOnly(libs.postgres)

    testImplementation(libs.spring.starter.test)
    testImplementation(libs.testcontainers.postgres)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.kotlin.mockito)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

flyway {
    schemas = arrayOf("nibas")
}

