pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "VConomy"
include("vconomy-common")
include("vconomy-spigot")
include("vconomy-paper")
include("vconomy-folia")