import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("poc.kmp-umbrella")
}

// Modules exportés dans l'API Swift de Shared.xcframework.
val exportedModules = listOf(
    ":shared:core",
    ":shared:features:auth",
    ":shared:features:auth-api",
    ":shared:features:catalog",
    ":shared:features:catalog-api",
    ":shared:features:cart",
    ":shared:features:cart-api",
)

kotlin {
    val xcFramework = XCFramework("Shared")
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "Shared"
            isStatic = true
            exportedModules.forEach { export(project(it)) }
            xcFramework.add(this)
        }
    }
    sourceSets {
        commonMain.dependencies {
            exportedModules.forEach { api(project(it)) }
        }
    }
}
