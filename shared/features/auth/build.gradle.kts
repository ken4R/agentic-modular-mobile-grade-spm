plugins {
    id("poc.kmp-feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:features:auth-api"))
            implementation(project(":shared:core"))
        }
    }
}
