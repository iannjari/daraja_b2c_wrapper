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

    // spring data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.1")

    // hibernate
    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")

    // flyway -DB migrations
    implementation("org.flywaydb:flyway-mysql:10.16.0")

    // kafka
    implementation("org.springframework.kafka:spring-kafka:3.2.2")

    // lombok
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // tests
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")

}

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
        exclude("org.springframework.boot", "logback-classic")
    }
}

tasks.test {
    useJUnitPlatform()
}