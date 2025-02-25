plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.allopen") version "1.9.22"
    kotlin("kapt") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.3"
    id("com.diffplug.spotless") version "6.23.3"
    id("io.micronaut.aot") version "4.3.3"
}

version = "0.1"
group = "com.mdodik.scart"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut.data:micronaut-data-processor")
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.security:micronaut-security-annotations")
    kapt("io.micronaut.validation:micronaut-validation-processor")

    implementation("io.micronaut.data:micronaut-data-mongodb")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("io.micronaut:micronaut-http-client")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")
    implementation("net.logstash.logback:logstash-logback-encoder:7.3")

    implementation("org.yaml:snakeyaml:1.14")

    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.mongodb:mongodb-driver-sync")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")


    implementation("org.litote.kmongo:kmongo-coroutine:4.8.0")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    aotPlugins("io.micronaut.security:micronaut-security-aot")

}


application {
    mainClass = "com.mdodik.scart.ApplicationKt"
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}


graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.mdodik.scart.*")
    }
    aot {
/*        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true*/
    }
}



