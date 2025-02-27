
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val h2_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
    id("jacoco")
}

group = "com.example"
version = "1.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")

    testImplementation("io.ktor:ktor-server-test-host:2.3.12")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.20")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.20")

    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
}

tasks.test {
    useJUnitPlatform() // Используем JUnit 5
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // Зависимость от задачи тестирования

    reports {
        xml.required.set(true) // Включить XML отчет
        html.required.set(true) // Включить HTML отчет
    }

}


jacoco {
    toolVersion = "0.8.12"
}
