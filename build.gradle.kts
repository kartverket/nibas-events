import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinPluginVersion = "1.9.22"

    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version kotlinPluginVersion
    kotlin("plugin.spring") version kotlinPluginVersion
    id("org.flywaydb.flyway") version "10.10.0"
}

group = "no.kartverket"
version = "0.0.1-SNAPSHOT"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

// Dependency versions
val SPRINGDOC_OPENAPI_VERSION2 = "2.3.0"
val LOGSTASH_VERSION = "7.4"
val FLYWAY_VERSION = "10.8.1"
val TEST_CONTAINER_VERSION = "1.19.6"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config")

    implementation("org.springdoc:springdoc-openapi-starter-common:$SPRINGDOC_OPENAPI_VERSION2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:$SPRINGDOC_OPENAPI_VERSION2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$SPRINGDOC_OPENAPI_VERSION2")

    implementation("net.logstash.logback:logstash-logback-encoder:$LOGSTASH_VERSION")

    runtimeOnly("org.flywaydb:flyway-core:$FLYWAY_VERSION")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$FLYWAY_VERSION")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:$TEST_CONTAINER_VERSION")
    testImplementation("org.testcontainers:junit-jupiter:$TEST_CONTAINER_VERSION")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
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
