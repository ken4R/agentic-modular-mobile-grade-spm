package poc.mobile.feature.catalog

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import poc.mobile.core.Outcome
import poc.mobile.feature.catalog.api.ProductSummary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogStoreTest {
    private fun TestScope.store(repository: CatalogRepository = InMemoryCatalogRepository()) =
        CatalogStore(repository, UnconfinedTestDispatcher(testScheduler))

    @Test
    fun initialStateIsEmpty() = runTest {
        assertEquals(CatalogUiState.Initial, store().state.value)
    }

    @Test
    fun loadPublishesProducts() = runTest {
        val store = store()
        store.accept(CatalogIntent.Load)
        assertEquals(listOf("p1", "p2", "p3"), store.state.value.products.map { it.id })
        assertFalse(store.state.value.isLoading)
        assertNull(store.state.value.error)
    }

    @Test
    fun loadFailureShowsError() = runTest {
        val store = store(FailingCatalogRepository)
        store.accept(CatalogIntent.Load)
        assertTrue(store.state.value.products.isEmpty())
        assertEquals("hors ligne", store.state.value.error)
    }

    @Test
    fun findProductResolvesKnownIdOnly() = runTest {
        val repository = InMemoryCatalogRepository()
        assertEquals("Café", repository.findProduct("p1")?.name)
        assertNull(repository.findProduct("inconnu"))
    }

    @Test
    fun priceLabelUsesEuroFormat() {
        assertEquals("4,50 €", ProductSummary(id = "p", name = "n", priceCents = 450).priceLabel)
        assertEquals("0,05 €", ProductSummary(id = "p", name = "n", priceCents = 5).priceLabel)
    }

    private object FailingCatalogRepository : CatalogRepository {
        override suspend fun products(): Outcome<List<ProductSummary>> = Outcome.Failure("hors ligne")
        override suspend fun findProduct(id: String): ProductSummary? = null
    }
}
