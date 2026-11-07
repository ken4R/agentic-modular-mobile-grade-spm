package poc.mobile.feature.cart

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import poc.mobile.feature.catalog.api.CatalogApi
import poc.mobile.feature.catalog.api.ProductSummary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CartStoreTest {
    private val repository = InMemoryCartRepository(FakeCatalogApi)

    private fun TestScope.store() = CartStore(repository, UnconfinedTestDispatcher(testScheduler))

    @Test
    fun addCreatesLineThenIncrementsQuantity() = runTest {
        val store = store()
        store.accept(CartIntent.Add("p1"))
        store.accept(CartIntent.Add("p1"))
        assertEquals(listOf(CartLine("p1", "Café", 450, 2)), store.state.value.lines)
        assertEquals("9,00 €", store.state.value.totalLabel)
        assertEquals(2, repository.itemCount.first())
    }

    @Test
    fun addUnknownProductShowsError() = runTest {
        val store = store()
        store.accept(CartIntent.Add("inconnu"))
        assertTrue(store.state.value.lines.isEmpty())
        assertEquals("Produit introuvable", store.state.value.error)
    }

    @Test
    fun decrementLowersQuantityThenRemovesLine() = runTest {
        val store = store()
        store.accept(CartIntent.Add("p1"))
        store.accept(CartIntent.Add("p1"))
        store.accept(CartIntent.Decrement("p1"))
        assertEquals(1, store.state.value.itemCount)
        store.accept(CartIntent.Decrement("p1"))
        assertTrue(store.state.value.lines.isEmpty())
    }

    @Test
    fun removeDropsLine() = runTest {
        val store = store()
        store.accept(CartIntent.Add("p1"))
        store.accept(CartIntent.Add("p2"))
        store.accept(CartIntent.Remove("p1"))
        assertEquals(listOf("p2"), store.state.value.lines.map { it.productId })
    }

    @Test
    fun clearEmptiesCart() = runTest {
        val store = store()
        store.accept(CartIntent.Add("p1"))
        store.accept(CartIntent.Clear)
        assertEquals(CartUiState.Initial, store.state.value)
        assertNull(store.state.value.error)
    }

    @Test
    fun storeReflectsAdditionsMadeElsewhere() = runTest {
        val store = store()
        repository.add("p2")
        assertEquals(listOf("p2"), store.state.value.lines.map { it.productId })
    }

    @Test
    fun concurrentAddsOfSameProductKeepOneLine() = runTest {
        val slowRepository = InMemoryCartRepository(SlowCatalogApi)
        listOf(launch { slowRepository.add("p1") }, launch { slowRepository.add("p1") }).joinAll()
        assertEquals(listOf(CartLine("p1", "Café", 450, 2)), slowRepository.lines.value)
    }

    /** Catalogue qui suspend, comme un vrai backend. */
    private object SlowCatalogApi : CatalogApi {
        override suspend fun findProduct(id: String): ProductSummary? {
            delay(100)
            return FakeCatalogApi.findProduct(id)
        }
    }

    private object FakeCatalogApi : CatalogApi {
        private val products = listOf(ProductSummary("p1", "Café", 450), ProductSummary("p2", "Croissant", 180))
        override suspend fun findProduct(id: String): ProductSummary? = products.firstOrNull { it.id == id }
    }
}
