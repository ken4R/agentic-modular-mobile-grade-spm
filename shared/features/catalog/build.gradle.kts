plugins {
    id("poc.kmp-feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:features:catalog-api"))
            implementation(project(":shared:core"))
        }
    }
}
