plugins {
    id("kmp-feature")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(project(":shared:features:catalog-api"))
        api(project(":shared:core"))
    }
}
