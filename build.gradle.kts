plugins {
    java
    `java-library`
    `java-gradle-plugin`
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.6"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

group = "dev.njari.daraja"
version = "1.0.0"

dependencies {
    // spring boot
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.1")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.6")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.3.1")
    implementation("org.springframework.boot:spring-boot-starter:3.3.1")
    implementation("org.springframework.boot:spring-boot-starter-security:3.3.1")

    // mariaDB
    implementation("org.mariadb.jdbc:mariadb-java-client:3.4.0")

    // tests
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}