package com.poc.catalog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CatalogStoreTest {
    @Test
    fun loadFillsProducts() = runTest {
        val store = CatalogStore(GetProductsUseCase(FakeCatalogRepository()), testStoreScope())
        store.dispatch(CatalogIntent.Load)
        advanceUntilIdle()
        assertEquals(3, store.state.value.products.size)
        assertFalse(store.state.value.isLoading)
    }
}

/** Scope du Store sur le scheduler de test, hors backgroundScope : advanceUntilIdle() exécute ses coroutines. */
private fun TestScope.testStoreScope() = CoroutineScope(StandardTestDispatcher(testScheduler))
