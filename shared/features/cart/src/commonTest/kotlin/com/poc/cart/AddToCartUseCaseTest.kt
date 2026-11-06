package com.poc.cart

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AddToCartUseCaseTest {
    private val useCase = AddToCartUseCase(FakeCatalogApi())

    @Test
    fun addsNewProduct() = runTest {
        val lines = useCase(emptyList(), "p1")
        assertEquals(listOf(CartLine("p1", "Café", 450, 1)), lines)
    }

    @Test
    fun incrementsExistingProduct() = runTest {
        val lines = useCase(useCase(emptyList(), "p1"), "p1")
        assertEquals(2, lines.single().quantity)
    }

    @Test
    fun ignoresUnknownProduct() = runTest {
        assertEquals(emptyList(), useCase(emptyList(), "unknown"))
    }
}
