import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinPluginVersion = "1.9.23"

    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version kotlinPluginVersion
    kotlin("plugin.spring") version kotlinPluginVersion
    id("org.flywaydb.flyway") version "10.17.1"
}

group = "no.kartverket"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

// Dependency versions
val SPRINGDOC_OPENAPI_VERSION2 = "2.5.0"
val LOGSTASH_VERSION = "8.0"
val FLYWAY_VERSION = "10.17.1"
val TEST_CONTAINER_VERSION = "1.20.1"
val PROMETHEUS_VERSION = "1.13.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springdoc:springdoc-openapi-starter-common:$SPRINGDOC_OPENAPI_VERSION2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:$SPRINGDOC_OPENAPI_VERSION2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$SPRINGDOC_OPENAPI_VERSION2")

    implementation("net.logstash.logback:logstash-logback-encoder:$LOGSTASH_VERSION")
    implementation("io.micrometer:micrometer-registry-prometheus:$PROMETHEUS_VERSION")

    runtimeOnly("org.flywaydb:flyway-core:$FLYWAY_VERSION")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$FLYWAY_VERSION")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:$TEST_CONTAINER_VERSION")
    testImplementation("org.testcontainers:junit-jupiter:$TEST_CONTAINER_VERSION")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
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

