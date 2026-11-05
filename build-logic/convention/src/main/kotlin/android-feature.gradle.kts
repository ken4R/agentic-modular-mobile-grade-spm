import com.android.build.api.dsl.LibraryExtension
import convention.configureAndroidCompose
import convention.configureDetekt

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

configureAndroidCompose(extensions.getByType<LibraryExtension>())
configureDetekt()
