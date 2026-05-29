plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.10"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.helpch.at/releases/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
}

dependencies {
    implementation(project(":vconomy-common"))
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.12.2")
}

tasks.shadowJar {
    relocate("com.zaxxer", "org.vaelow233.vconomy.libs.com.zaxxer")
    relocate("org.yaml", "org.vaelow233.vconomy.libs.org.yaml")
}