plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.spotbugs") version "6.0.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "lab1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotbugs {
    toolVersion.set("4.8.5")
    effort.set(com.github.spotbugs.snom.Effort.MAX)
    reportLevel.set(com.github.spotbugs.snom.Confidence.LOW)
}


tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports {
        create("xml") {
            required.set(true)
            outputLocation.set(layout.buildDirectory.file("reports/spotbugs/${name.removePrefix("spotbugs") .lowercase()}/spotbugs.xml"))
        }

        create("sarif") {
            required.set(true)
            outputLocation.set(layout.buildDirectory.file("reports/spotbugs/${name.removePrefix("spotbugs").lowercase()}/spotbugs.sarif"))
        }
    }
}

tasks.check { dependsOn("spotbugsMain") }