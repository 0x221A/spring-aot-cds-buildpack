import org.springframework.boot.buildpack.platform.build.PullPolicy
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
    environment.putAll(
        mapOf(
            "BP_JVM_VERSION" to "21",
            "BP_SPRING_AOT_ENABLED" to "true",
            "BP_JVM_CDS_ENABLED" to "true",
        ),
    )
    buildpacks.set(
        listOf(
            "gcr.io/paketo-buildpacks/ca-certificates:3.10",
            "gcr.io/paketo-buildpacks/azul-zulu:11.2",
            "gcr.io/paketo-buildpacks/syft:2.10",
            "gcr.io/paketo-buildpacks/executable-jar:6.13",
            "gcr.io/paketo-buildpacks/spring-boot:5.33",
            "gcr.io/paketo-buildpacks/java:18.4",
        ),
    )
}
