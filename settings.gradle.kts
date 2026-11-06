pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
<<<<<<< HEAD
=======
    // Aucun module ne déclare ses propres dépôts.
>>>>>>> origin/main
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

<<<<<<< HEAD
rootProject.name = "agentic-modular-mobile-grade-spm"

// Logique partagée KMP. :shared est le module parapluie qui produit Shared.xcframework.
include(":shared")
include(":shared:core")
include(":shared:konsist") // tests d'architecture, lancés par scripts/check-architecture.sh
listOf("auth", "catalog", "cart").forEach { feature ->
    include(":shared:features:$feature")
    include(":shared:features:$feature-api")
}

// UI Android. Les projets portent le suffixe -ui pour ne pas avoir le même nom
// que le module KMP de la feature (collision des fichiers .kotlin_module à l'empaquetage).
include(":android:app")
listOf("auth", "catalog", "cart").forEach { feature ->
    include(":android:features:$feature-ui")
    project(":android:features:$feature-ui").projectDir = file("android/features/$feature")
}
=======
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
>>>>>>> origin/main
