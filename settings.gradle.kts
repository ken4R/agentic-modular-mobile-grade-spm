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

// Cache local toujours actif ; cache distant HTTP (plan-action §2, semaine 2) si GRADLE_REMOTE_CACHE_URL est défini.
// Écriture seulement en CI (ou GRADLE_REMOTE_CACHE_PUSH=true) : les postes de dev lisent, la CI remplit.
// HTTP en clair accepté uniquement sur localhost (serveur de test scripts/http-build-cache.py).
val remoteCacheUrl = providers.environmentVariable("GRADLE_REMOTE_CACHE_URL").orNull
val remoteCachePush = providers.environmentVariable("GRADLE_REMOTE_CACHE_PUSH")
    .orElse(providers.environmentVariable("CI")).orNull == "true"
val remoteCacheUser = providers.environmentVariable("GRADLE_REMOTE_CACHE_USERNAME").orNull
val remoteCachePassword = providers.environmentVariable("GRADLE_REMOTE_CACHE_PASSWORD").orNull
buildCache {
    local { isEnabled = true }
    if (!remoteCacheUrl.isNullOrBlank()) {
        remote<HttpBuildCache> {
            val remoteUri = uri(remoteCacheUrl)
            url = remoteUri
            isAllowInsecureProtocol = remoteUri.host in setOf("localhost", "127.0.0.1")
            isPush = remoteCachePush
            if (remoteCacheUser != null) {
                credentials {
                    username = remoteCacheUser
                    password = remoteCachePassword
                }
            }
        }
    }
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
