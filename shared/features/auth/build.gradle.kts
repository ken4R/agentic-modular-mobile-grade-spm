plugins {
    id("kmp-feature")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(project(":shared:features:auth-api"))
        api(project(":shared:core"))
    }
}
