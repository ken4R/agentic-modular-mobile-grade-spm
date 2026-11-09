import convention.configureDetekt
import convention.configureKmpLibrary
import convention.configureKmpTests
import convention.configureProjectGraph

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

configureKmpLibrary()
configureKmpTests()
configureDetekt()
configureProjectGraph("allTests")
