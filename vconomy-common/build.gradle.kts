plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.yaml:snakeyaml:2.6")
    compileOnly("com.mysql:mysql-connector-j:9.7.0")
    compileOnly("com.h2database:h2:2.4.240")
    compileOnly("org.xerial:sqlite-jdbc:3.43.0.0")
    compileOnly("org.postgresql:postgresql:42.7.11")
}

tasks.shadowJar {
    relocate("com.zaxxer", "org.vaelow233.vconomy.libs.com.zaxxer")
    relocate("org.yaml", "org.vaelow233.vconomy.libs.org.yaml")
}