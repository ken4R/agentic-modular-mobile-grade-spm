dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
<<<<<<< HEAD
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
=======
        create("libs") { from(files("../gradle/libs.versions.toml")) }
>>>>>>> origin/main
    }
}

rootProject.name = "build-logic"
include(":convention")
