package com.poc.cart

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CartStoreTest {
    private fun TestScope.store() = CartStore(AddToCartUseCase(FakeCatalogApi()), backgroundScope)

    @Test
    fun addUpdatesCountAndTotal() = runTest {
        val store = store()
        store.dispatch(CartIntent.Add("p1"))
        advanceUntilIdle()
        store.dispatch(CartIntent.Add("p2"))
        advanceUntilIdle()
        assertEquals(2, store.state.value.itemCount)
        assertEquals(630L, store.state.value.totalCents)
    }

    @Test
    fun removeDeletesLine() = runTest {
        val store = store()
        store.dispatch(CartIntent.Add("p1"))
        advanceUntilIdle()
        store.dispatch(CartIntent.Remove("p1"))
        assertEquals(0, store.state.value.itemCount)
    }

    @Test
    fun clearEmptiesCart() = runTest {
        val store = store()
        store.dispatch(CartIntent.Add("p1"))
        advanceUntilIdle()
        store.dispatch(CartIntent.Clear)
        assertEquals(CartUiState(), store.state.value)
    }
}
