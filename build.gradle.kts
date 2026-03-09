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
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.1")
    }
    dependencies {
        dependency("tools.jackson.core:jackson-core:${libs.versions.jacksonVersion.get()}")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

buildscript {
    configurations.classpath {
        resolutionStrategy {
            // TODO: Fjern tvungen jackson-versjon når flyway ikke lenger bruker en sårbar versjon av jackson
            force("tools.jackson.core:jackson-core:${libs.versions.jacksonVersion.get()}")
            force("tools.jackson.core:jackson-databind:${libs.versions.jacksonVersion.get()}")
        }
    }
    dependencies {
        classpath(libs.flyway.postgres)
    }
}

dependencies {
    implementation(libs.spring.webmvc)
    implementation(libs.spring.jdbc)
    implementation(libs.spring.security) // Web-security
    implementation(libs.jackson.core)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.stdlib.jdk)
    implementation(libs.spring.actuator)
    implementation(libs.spring.validator)

    implementation(libs.springdoc.openapi.common)
    implementation(libs.springdoc.openapi.webmvc.api)
    implementation(libs.springdoc.openapi.webmvc.ui)

    implementation(libs.logstash.logback.encoder)
    implementation(libs.micrometer.registry.prometheus)

    implementation(libs.spring.flyway)
    runtimeOnly(libs.flyway.postgres)
    runtimeOnly(libs.postgres)

    testImplementation(libs.spring.starter.test)
    testImplementation(libs.spring.webmvc.test)
    testImplementation(libs.testcontainers.postgres)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.kotlin.mockito)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
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

