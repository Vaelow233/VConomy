plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.10"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.helpch.at/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":vconomy-common"))
    implementation(project(":vconomy-spigot"))
    compileOnly("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.12.2")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks.shadowJar {
    relocate("com.zaxxer", "org.vaelow233.vconomy.libs.com.zaxxer")
    relocate("org.yaml", "org.vaelow233.vconomy.libs.org.yaml")
}