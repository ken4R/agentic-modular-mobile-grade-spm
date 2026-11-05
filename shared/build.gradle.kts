import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
<<<<<<< HEAD
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
=======
    id("kmp-umbrella")
}

// Modules visibles depuis Swift. Toute nouvelle feature s'ajoute ici (skill new-feature-module).
val exportedModules = listOf(
    ":shared:core",
    ":shared:features:auth-api",
    ":shared:features:auth",
>>>>>>> origin/main
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
<<<<<<< HEAD
    sourceSets {
        commonMain.dependencies {
            exportedModules.forEach { api(project(it)) }
        }
=======
    sourceSets.commonMain.dependencies {
        exportedModules.forEach { api(project(it)) }
>>>>>>> origin/main
    }
}
