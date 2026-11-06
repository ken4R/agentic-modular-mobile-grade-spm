plugins {
<<<<<<< HEAD
    id("poc.kmp-feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:features:auth-api"))
            implementation(project(":shared:core"))
        }
=======
    id("kmp-feature")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(project(":shared:features:auth-api"))
        api(project(":shared:core"))
>>>>>>> origin/main
    }
}
