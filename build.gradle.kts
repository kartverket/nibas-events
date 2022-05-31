import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	id("io.gitlab.arturbosch.detekt") version "1.20.0"
}

group = "no.kartverket"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    mavenCentral()
}

object DependencyVersions {
	const val SPRINGDOC_OPENAPI_VERSION = "1.6.9"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springdoc:springdoc-openapi-webmvc-core:${DependencyVersions.SPRINGDOC_OPENAPI_VERSION}")
	implementation("org.springdoc:springdoc-openapi-kotlin:${DependencyVersions.SPRINGDOC_OPENAPI_VERSION}")
	implementation("org.springdoc:springdoc-openapi-ui:${DependencyVersions.SPRINGDOC_OPENAPI_VERSION}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
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

detekt {
	config = files("detekt.yml")
}

tasks.withType<Detekt>().configureEach {
	reports {
		sarif.required.set(true)
	}
}
