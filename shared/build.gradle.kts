import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("kmp-umbrella")
}

// Modules visibles depuis Swift. Toute nouvelle feature s'ajoute ici (skill new-feature-module).
val exportedModules = listOf(
    ":shared:core",
    ":shared:features:auth-api",
    ":shared:features:auth",
    ":shared:features:catalog-api",
    ":shared:features:catalog",
    ":shared:features:cart-api",
    ":shared:features:cart",
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
    sourceSets.commonMain.dependencies {
        exportedModules.forEach { api(project(it)) }
    }
}
