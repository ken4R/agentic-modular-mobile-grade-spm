plugins {
    id("kmp-api")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.coroutines.core)
    }
}
