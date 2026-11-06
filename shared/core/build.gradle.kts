plugins {
<<<<<<< HEAD
    id("poc.kmp-core")
=======
    id("kmp-core")
>>>>>>> origin/main
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.coroutines.core)
            api(libs.koin.core)
            api(libs.ktor.client.core)
<<<<<<< HEAD
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            api(libs.sqldelight.runtime)
=======
            api(libs.sqldelight.runtime)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
>>>>>>> origin/main
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
<<<<<<< HEAD

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.poc.core.db")
        }
    }
}
=======
>>>>>>> origin/main
