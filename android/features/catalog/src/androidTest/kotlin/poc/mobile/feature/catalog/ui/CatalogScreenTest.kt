package poc.mobile.feature.catalog.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import poc.mobile.core.Outcome
import poc.mobile.feature.catalog.CatalogRepository
import poc.mobile.feature.catalog.CatalogStore
import poc.mobile.feature.catalog.api.ProductSummary

class CatalogScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun productsAreListedAndAddForwardsId() {
        val added = mutableListOf<String>()
        val repository = FakeCatalogRepository(Outcome.Success(listOf(ProductSummary("p1", "Café", 450))))
        val store = CatalogStore(repository, Dispatchers.Main)
        compose.setContent { CatalogScreen(store, onAddToCart = { added += it }) }
        compose.onNodeWithTag(CatalogTags.product("p1")).assertIsDisplayed()
        compose.onNodeWithTag(CatalogTags.add("p1")).performClick()
        assertEquals(listOf("p1"), added)
    }

    @Test
    fun failureShowsError() {
        val store = CatalogStore(FakeCatalogRepository(Outcome.Failure("hors ligne")), Dispatchers.Main)
        compose.setContent { CatalogScreen(store, onAddToCart = {}) }
        compose.onNodeWithTag(CatalogTags.ERROR).assertIsDisplayed()
    }

    private class FakeCatalogRepository(private val result: Outcome<List<ProductSummary>>) : CatalogRepository {
        override suspend fun products(): Outcome<List<ProductSummary>> = result
        override suspend fun findProduct(id: String): ProductSummary? = null
    }
}
