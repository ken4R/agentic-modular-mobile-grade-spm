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
