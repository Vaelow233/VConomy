plugins {
    id("java")
}

allprojects {
    apply(plugin = "java")
    group = properties["group"]!!
    version = properties["version"]!!

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = "8"
        targetCompatibility = "8"
    }
}

sourceSets {
    main {}
    test {}
}