plugins {
    id("poc.kmp-core")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.coroutines.core)
            api(libs.koin.core)
            api(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            api(libs.sqldelight.runtime)
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

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.poc.core.db")
        }
    }
}
