plugins {
    id("kmp-core")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.coroutines.core)
            api(libs.koin.core)
            api(libs.ktor.client.core)
            api(libs.sqldelight.runtime)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sqldelight.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.native)
        }
    }
}
