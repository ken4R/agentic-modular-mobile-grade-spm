package poc.mobile.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertFalse
import poc.mobile.konsist.ArchitectureRules.FEATURE_PACKAGE
import poc.mobile.konsist.ArchitectureRules.FORBIDDEN_IN_UI
import poc.mobile.konsist.ArchitectureRules.featureOf
import poc.mobile.konsist.ArchitectureRules.gradleViolations
import poc.mobile.konsist.ArchitectureRules.isForeignFeatureImport
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class DependencyRulesTest {
    private val files = Konsist.scopeFromProject().files

    @Test
    fun coreDoesNotImportFeatures() {
        files.filter { it.path.contains("/shared/core/") }
            .assertFalse { it.hasImport { import -> import.hasNameStartingWith(FEATURE_PACKAGE) } }
    }

    @Test
    fun featureImportsOnlyItselfOrApis() {
        val featureFiles = files.filter { featureOf(it.path) != null }
        if (featureFiles.isEmpty()) return
        featureFiles.assertFalse { file ->
            val own = requireNotNull(featureOf(file.path))
            file.imports.any { isForeignFeatureImport(it.name, own) }
        }
    }

    @Test
    fun uiModulesDoNotTouchNetworkOrDatabase() {
        val uiFiles = files.filter { it.path.contains("/android/") }
        if (uiFiles.isEmpty()) return
        uiFiles.assertFalse { it.hasImport { import -> FORBIDDEN_IN_UI.any(import::hasNameStartingWith) } }
    }

    @Test
    fun gradleModulesDoNotDependOnAnotherFeature() {
        val root = File(requireNotNull(System.getProperty("projectRoot")))
        val violations = root.walkTopDown()
            .onEnter { it.name != "build" && !it.name.startsWith(".") }
            .filter { it.name == "build.gradle.kts" }
            .flatMap { gradleViolations(it.relativeTo(root).invariantSeparatorsPath, it.readText()) }
            .toList()
        assertTrue(violations.isEmpty(), violations.joinToString("\n"))
    }
}
