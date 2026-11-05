import com.android.build.api.dsl.ApplicationExtension
import convention.TARGET_SDK
import convention.configureAndroidCompose
import convention.configureDetekt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

extensions.configure<ApplicationExtension> {
    configureAndroidCompose(this)
    defaultConfig {
        applicationId = namespace
        targetSdk = TARGET_SDK
        versionCode = 1
        versionName = "0.1.0"
    }
}

configureDetekt()
