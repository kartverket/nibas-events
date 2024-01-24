import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinPluginVersion = "1.8.21"

    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version kotlinPluginVersion
    kotlin("plugin.spring") version kotlinPluginVersion
    id("org.flywaydb.flyway") version "10.6.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

group = "no.kartverket"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    mavenCentral()
}

// Dependency versions
val SPRINGDOC_OPENAPI_VERSION2 = "2.1.0"
val LOGSTASH_VERSION = "7.4"

ext["snakeyaml.version"] = "1.32"
ext["jackson-databind.version"] = "2.13.4.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config:4.0.1")

    implementation("org.springdoc:springdoc-openapi-starter-common:$SPRINGDOC_OPENAPI_VERSION2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:$SPRINGDOC_OPENAPI_VERSION2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$SPRINGDOC_OPENAPI_VERSION2")

    implementation("org.flywaydb:flyway-core")

    implementation("net.logstash.logback:logstash-logback-encoder:$LOGSTASH_VERSION")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:1.19.1")
    testImplementation("org.testcontainers:junit-jupiter:1.19.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
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
