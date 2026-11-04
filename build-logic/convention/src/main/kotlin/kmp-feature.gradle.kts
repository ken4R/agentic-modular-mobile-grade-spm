import convention.configureKmpLibrary
import convention.configureKmpTests

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

configureKmpLibrary()
configureKmpTests()
