pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Aucun module ne déclare ses propres dépôts.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Cache local seul en semaine 1 ; le cache distant arrive en semaine 2 (plan-action §2).
buildCache {
    local { isEnabled = true }
}

rootProject.name = "agentic-modular-mobile-grade-spm"
include(":shared:core")
include(":shared:konsist")
include(":android:app")
include(":shared:features:auth-api")
include(":shared:features:auth")
include(":android:features:auth")
include(":shared:features:catalog-api")
include(":shared:features:catalog")
include(":android:features:catalog")
include(":shared:features:cart-api")
include(":shared:features:cart")
include(":android:features:cart")
