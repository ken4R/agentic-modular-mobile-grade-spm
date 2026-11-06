package com.poc.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertFalse
import kotlin.test.Test

/** Règles de dépendances d'AGENTS.md, vérifiées sur les imports de tout le monorepo. */
class ArchitectureTest {
    private val features = listOf("auth", "catalog", "cart")
    private val files = Konsist.scopeFromProject().files

    private fun filesUnder(dir: String) = files.filter { "/$dir/" in it.path }

    @Test
    fun featureNeverDependsOnAnotherFeature() {
        features.forEach { feature ->
            val others = features - feature
            val featureFiles = filesUnder("shared/features/$feature") + filesUnder("android/features/$feature")
            featureFiles.assertFalse(additionalMessage = "$feature ne doit consommer que les modules -api") { file ->
                file.imports.any { import ->
                    others.any { other ->
                        import.name.startsWith("com.poc.$other.") && !import.name.startsWith("com.poc.$other.api.")
                    }
                }
            }
        }
    }

    @Test
    fun coreDependsOnNoFeature() {
        filesUnder("shared/core").assertFalse { file ->
            file.imports.any { import -> features.any { import.name.startsWith("com.poc.$it.") } }
        }
    }

    @Test
    fun uiModulesHaveNoNetworkNorDatabaseAccess() {
        val forbidden = listOf("io.ktor.", "app.cash.sqldelight.", "com.poc.core.network.", "com.poc.core.db.")
        filesUnder("android/features").assertFalse { file ->
            file.imports.any { import -> forbidden.any { import.name.startsWith(it) } }
        }
    }
}
