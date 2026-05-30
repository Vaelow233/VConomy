plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.10"
}

group = "org.vaelow233.vconomy"
version = "0.0.2"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.helpch.at/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":vconomy-common"))
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.12.2")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}