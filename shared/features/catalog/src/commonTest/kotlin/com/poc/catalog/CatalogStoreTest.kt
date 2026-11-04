package com.poc.catalog

import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CatalogStoreTest {
    @Test
    fun loadFillsProducts() = runTest {
        val store = CatalogStore(GetProductsUseCase(FakeCatalogRepository()), backgroundScope)
        store.dispatch(CatalogIntent.Load)
        advanceUntilIdle()
        assertEquals(3, store.state.value.products.size)
        assertFalse(store.state.value.isLoading)
    }
}
