
plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
}

group = "com.zenmo"
version = "0.0.1"

application {
    mainClass.set("com.zenmo.ApplicationKt")

    // This argument is only applied when run through Gradle,
    // not when building and running a Fat Jar.
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

repositories {
    mavenCentral()
}

val exposed_version = "0.43.0"

dependencies {
    implementation(platform("com.azure:azure-sdk-bom:1.2.18"))
    implementation("com.azure:azure-storage-blob:12.25.0")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("com.h2database:h2:2.1.214")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.4")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.4")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.20")
}

tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}
