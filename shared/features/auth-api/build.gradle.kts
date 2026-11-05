plugins {
<<<<<<< HEAD
    id("poc.kmp-feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:core"))
        }
=======
    id("kmp-api")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.coroutines.core)
>>>>>>> origin/main
    }
}
