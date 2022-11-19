import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    id("org.flywaydb.flyway") version "9.8.2"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
}

group = "no.kartverket"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    mavenCentral()
}

// Dependency versions
val SPRINGDOC_OPENAPI_VERSION = "1.6.9"
val SPRING_CLOUD_GCP_STARTER = "3.4.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config:3.1.1")

    implementation("org.springdoc:springdoc-openapi-webflux-core:$SPRINGDOC_OPENAPI_VERSION")
    implementation("org.springdoc:springdoc-openapi-kotlin:$SPRINGDOC_OPENAPI_VERSION")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:$SPRINGDOC_OPENAPI_VERSION")

    implementation("com.google.cloud:spring-cloud-gcp-starter:$SPRING_CLOUD_GCP_STARTER")
    implementation("com.google.cloud:spring-cloud-gcp-pubsub:$SPRING_CLOUD_GCP_STARTER")
    implementation("org.springframework.integration:spring-integration-core")

    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework:spring-jdbc")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
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

detekt {
    config = files("detekt.yml")
}

tasks.withType<Detekt>().configureEach {
    reports {
        sarif.required.set(true)
    }
}
