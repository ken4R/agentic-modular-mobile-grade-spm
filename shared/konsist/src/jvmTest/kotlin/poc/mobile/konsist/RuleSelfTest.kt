package poc.mobile.konsist

import poc.mobile.konsist.ArchitectureRules.gradleViolations
import poc.mobile.konsist.ArchitectureRules.isForeignFeatureImport
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Les règles doivent détecter une violation : sans features, les tests de DependencyRulesTest passent à vide. */
class RuleSelfTest {
    @Test
    fun detectsFeatureToFeatureGradleDependency() {
        val script = """implementation(project(":shared:features:cart"))"""
        assertEquals(1, gradleViolations("shared/features/catalog/build.gradle.kts", script).size)
    }

    @Test
    fun allowsApiAndOwnGradleDependency() {
        val script = """implementation(project(":shared:features:cart-api")) implementation(project(":shared:features:catalog"))"""
        assertEquals(0, gradleViolations("android/features/catalog/build.gradle.kts", script).size)
    }

    @Test
    fun detectsCoreDependingOnFeature() {
        val script = """implementation(project(":shared:features:cart-api"))"""
        assertEquals(1, gradleViolations("shared/core/build.gradle.kts", script).size)
    }

    @Test
    fun classifiesImports() {
        assertTrue(isForeignFeatureImport("poc.mobile.feature.cart.CartStore", "catalog"))
        assertFalse(isForeignFeatureImport("poc.mobile.feature.cart.api.CartApi", "catalog"))
        assertFalse(isForeignFeatureImport("poc.mobile.feature.catalog.CatalogStore", "catalog"))
        assertFalse(isForeignFeatureImport("poc.mobile.core.Outcome", "catalog"))
    }
}
