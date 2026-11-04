plugins {
    id("poc.kmp-feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:core"))
        }
    }
}
